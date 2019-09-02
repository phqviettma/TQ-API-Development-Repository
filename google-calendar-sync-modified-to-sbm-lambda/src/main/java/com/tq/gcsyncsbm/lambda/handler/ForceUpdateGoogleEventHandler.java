package com.tq.gcsyncsbm.lambda.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.model.GeneralAppt;
import com.tq.googlecalendar.model.PractitionerApptGroup;
import com.tq.googlecalendar.model.PractitionerApptGroup.EventDateInfo;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.googlecalendar.time.TimeUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ForceUpdateGoogleEventHandler {
	private static final Logger m_logger = LoggerFactory.getLogger(ForceUpdateGoogleEventHandler.class);
	private static final Integer MAX_RESULTS = 100;
	private static final String GOOGLE_AGENT = "google";
	private static final String SBM_AGENT = "sbm";

	private Env m_env = null;
	private GoogleCalendarApiServiceBuilder m_apiServiceBuilder = null;
	private SbmGoogleCalendarDbService m_sbmCalendarService = null;
	private SbmBreakTimeManagement m_sbmBreakTimeManagement = null;
	private GoogleCalendarDbService m_googleCalendarService = null;
	private TokenGoogleCalendarService m_tokenCalendarService = null;
	private GoogleCalendarModifiedSyncService m_modifiedChannelService = null;
	private SbmUnitService m_unitService = null;
	private SpecialdayServiceSbm m_specialDayService = null;
	private TokenServiceSbm m_tokenService = null;

	public ForceUpdateGoogleEventHandler(Env env, SbmGoogleCalendarDbService sbmCalendarService,
			GoogleCalendarApiServiceBuilder apiServiceBuilder, SbmBreakTimeManagement sbmBreakTimeManagement,
			GoogleCalendarDbService googleCalendarService, TokenGoogleCalendarService tokenCalendarService,
			GoogleCalendarModifiedSyncService modifiedChannelService, SbmUnitService unitService,
			SpecialdayServiceSbm specialDayService, TokenServiceSbm tokenService) {
		m_env = env;
		m_apiServiceBuilder = apiServiceBuilder;
		m_sbmBreakTimeManagement = sbmBreakTimeManagement;
		m_sbmCalendarService = sbmCalendarService;
		m_googleCalendarService = googleCalendarService;
		m_modifiedChannelService = modifiedChannelService;
		m_tokenCalendarService = tokenCalendarService;
		m_unitService = unitService;
		m_specialDayService = specialDayService;
		m_tokenService = tokenService;
	}

	public void handle(String googleCalendarId, Set<String> dateToBeUpdated)
			throws GoogleApiSDKException, NumberFormatException, SbmSDKException {
		GCModifiedChannel channelInfo = m_modifiedChannelService.load(googleCalendarId);
		if (channelInfo == null) {
			m_logger.info("{} does not exist in GCModifiedChannel", googleCalendarId);
			return;
		}
		List<GoogleCalendarSbmSync> googleCalendarInfos = m_googleCalendarService.queryEmail(channelInfo.getEmail());

		if (googleCalendarInfos.isEmpty()) {
			m_logger.info("{} does not exist in GoogleCalendarSbmSync", channelInfo.getEmail());
			return;
		}
		GoogleCalendarSbmSync googleCalendarInfo = googleCalendarInfos.get(0);

		TokenReq tokenReq = new TokenReq(m_env.getGoogleClientId(), m_env.getGoogleClientSecrets(),
				googleCalendarInfo.getRefreshToken());
		TokenResp token = m_tokenCalendarService.getToken(tokenReq);
		GoogleCalendarApiService googleApiService = m_apiServiceBuilder.build(token.getAccess_token());
		GoogleCalendarSettingsInfo settingInfo = googleApiService.getSettingInfo("timezone");
		for (String date : dateToBeUpdated) {
			CalendarEvents eventList = getEventListByDate(googleApiService, settingInfo.getValue(),
					googleCalendarInfo.getGoogleCalendarId(), date);
			m_logger.info("Fetched Google Events: {}", eventList);
			List<Items> confirmedItems = new ArrayList<>();
			List<Items> cancelledItems = new ArrayList<>();
			
			for(Items items : eventList.getItems()) {
				if ("confirmed".equals(items.getStatus())) {
					confirmedItems.add(items);
				} else if ("cancelled".equals(items.getStatus())) {
					cancelledItems.add(items);
				}
			}
			
			forceAddAppointment(confirmedItems, googleCalendarInfo);
			
		}
	}

	private CalendarEvents getEventListByDate(GoogleCalendarApiService googleApiService, String timeZoneId,
			String googleCalendarId, String date) throws GoogleApiSDKException {
		
		Instant instant = new Instant();
		DateTime timeMin = instant.parse(date).toDateTime().toLocalDateTime().toDateTime(DateTimeZone.forID(timeZoneId));
		DateTime timeMax = timeMin.plusDays(1);
		m_logger.info("date = {}, timeZoneId = {}, timeMin = {}, timeMax = {}", date, timeZoneId, timeMin, timeMax);
		boolean showDeleted = true;

		return googleApiService.getEventWithoutTokenByTimeMinAndTimeMax(MAX_RESULTS, timeMin.toString(),
				timeMax.toString(), showDeleted, googleCalendarId);
	}
	
	private void forceAddAppointment(List<Items> eventList, GoogleCalendarSbmSync googleCalendarInfo) throws NumberFormatException, SbmSDKException {
		PractitionerApptGroup apptGroup = new PractitionerApptGroup();
		for (Items event : eventList) {
			if (event.getStart().getDateTime() == null || event.getEnd().getDateTime() == null) {
				m_logger.info("Don't support modify for event has no start/end date time, ignoring: {}", event);
				continue;
			}
			SbmGoogleCalendar sbmGoogleSync = m_sbmCalendarService.queryWithIndex(event.getId());
			if (sbmGoogleSync != null && SBM_AGENT.equalsIgnoreCase(sbmGoogleSync.getAgent())) {
				m_logger.warn("Skipping appointment id {} due to booked from SBM", sbmGoogleSync.getAgent());
				continue;
			}
			String date = TimeUtils.extractDate(event.getStart().getDateTime());
			apptGroup.addAppt(date,
					new GeneralAppt(event.getStart().getDateTime(), event.getEnd().getDateTime(), event));
		}
		
		m_logger.info(apptGroup.toString());
		if (!apptGroup.getAppts().isEmpty()) {
			String sbmId = googleCalendarInfo.getSbmId();
			String userToken = getUserToken();
			String unitId[] = sbmId.split("-");
			changeBreakTime(apptGroup, userToken, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]), true);
			updateDb(apptGroup);
		}
	}

	private void updateDb(PractitionerApptGroup apptGroup) {
		for (Entry<String, EventDateInfo> entry : apptGroup.getEventDateInfoMap().entrySet()) {
			List<Items> eventList = entry.getValue().googleEvents;
			for (Items event : eventList) {
				SbmGoogleCalendar sbmGoogleSync = m_sbmCalendarService.queryWithIndex(event.getId());
				if (sbmGoogleSync == null) {
					m_logger.info("{} does not exist in SbmGoogleCalendar, creating it", event.getId());
					UUID uuid = UUID.randomUUID();
					long bookingId = uuid.getMostSignificantBits();
					SbmGoogleCalendar newSbmGoogleSync = new SbmGoogleCalendar(bookingId, event.getId(), 1,
							GOOGLE_AGENT, event.getOrganizer().getEmail(), event.getUpdated(),
							event.getStart().getDateTime(), event.getEnd().getDateTime());
					m_sbmCalendarService.put(newSbmGoogleSync);
					m_logger.info("Created {} with value {}", event.getId(), newSbmGoogleSync);
				} else {
					sbmGoogleSync.setStartDateTime(event.getStart().getDateTime());
					sbmGoogleSync.setEndDateTime(event.getEnd().getDateTime());
					sbmGoogleSync.setUpdated(event.getUpdated());
					m_sbmCalendarService.put(sbmGoogleSync);
					m_logger.info("Update to database successfully with value " + sbmGoogleSync);
				}
			}
		}
	}

	private void changeBreakTime(PractitionerApptGroup group, String token, Integer unitId, Integer eventId, boolean isForcedAdd)
			throws SbmSDKException {

		Map<String, UnitWorkingTime> unitWorkingDayInfoMap = m_unitService.getUnitWorkDayInfo(
				m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(), token, group.getStartDateString(),
				group.getEndDateString(), unitId);
		UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(group.getStartDateString());
		Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
		WorkingTime workingTime = unitWorkingTimeMap.get(String.valueOf(unitId));
		Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = m_specialDayService.getWorkDaysInfo(
				m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
				new FromDate(group.getStartDateString(), workingTime.getStart_time()),
				new ToDate(group.getEndDateString(), workingTime.getEnd_time()));

		for (Entry<String, EventDateInfo> entry : group.getEventDateInfoMap().entrySet()) {
			Set<Breaktime> breakTimes = entry.getValue().breakTimeSet;
			String date = entry.getKey();
			
			if (!breakTimes.isEmpty()) {
				if (isForcedAdd) {
    				m_sbmBreakTimeManagement.addBreakTime(m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(),
    						token, unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date,
    						breakTimes, workDayInfoMapForUnitId);
				}
			}
		}
	}
	
	private String getUserToken() throws SbmSDKException {
		String companyLogin = m_env.getSimplyBookCompanyLogin();
		String endpointLogin = m_env.getSimplyBookServiceUrlLogin();
		String password = m_env.getSimplyBookPassword();
		String username = m_env.getSimplyBookUser();
		return m_tokenService.getUserToken(companyLogin, username, password, endpointLogin);
	}
}
