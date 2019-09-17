package com.tq.gcsyncsbm.lambda.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.model.GeneralAppt;
import com.tq.googlecalendar.model.PractitionerApptGroup;
import com.tq.googlecalendar.model.PractitionerApptGroup.EventDateInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.time.TimeUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateGoogleEventHandler implements GCInternalHandler {
	private static final Logger m_log = LoggerFactory.getLogger(CreateGoogleEventHandler.class);
	private Env env = null;
	private TokenServiceSbm tokenService = null;
	private SpecialdayServiceSbm specialdayService = null;
	private SbmBreakTimeManagement sbmBreakTimeManagement = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private SbmUnitService unitService = null;
	private ForceUpdateGoogleEventHandler forceEventHandler = null;
	private ChangeGoogleEventHandler changeEventHandler = null;
	private static final String GOOGLE = "google";
	private static final String SBM = "sbm";

	public CreateGoogleEventHandler(Env env, TokenServiceSbm tss, SpecialdayServiceSbm sds, SbmBreakTimeManagement sbt,
			SbmGoogleCalendarDbService sdcs, SbmUnitService uss, ForceUpdateGoogleEventHandler forceEventHandler,
			ChangeGoogleEventHandler changeEventHandler) {
		this.env = env;
		this.tokenService = tss;
		this.specialdayService = sds;
		this.sbmBreakTimeManagement = sbt;
		this.sbmCalendarService = sdcs;
		this.unitService = uss;
		this.forceEventHandler = forceEventHandler;
		this.changeEventHandler = changeEventHandler;
	}

	@Override
	public void handle(List<Items> item, String sbmId, GoogleCalendarSbmSync googleCalendarSbmSync) throws SbmSDKException, InfSDKExecption {
		syncToSbm(item, sbmId, googleCalendarSbmSync);
	}

	private boolean syncToSbm(List<Items> eventItems, String sbmId, GoogleCalendarSbmSync googleCalendarSbmSync) throws SbmSDKException {
		String companyLogin = env.getSimplyBookCompanyLogin();
		String endpointLogin = env.getSimplyBookServiceUrlLogin();
		String endpoint = env.getSimplyBookAdminServiceUrl();
		String password = env.getSimplyBookPassword();
		String username = env.getSimplyBookUser();
		String unitId[] = sbmId.split("-");

		String token = tokenService.getUserToken(companyLogin, username, password, endpointLogin);
		PractitionerApptGroup apptGroup = new PractitionerApptGroup();
		Set<String> dateToBeUpdated = new HashSet<String>();
		String googleCalendarId = googleCalendarSbmSync.getGoogleCalendarId();
		boolean isResync = googleCalendarSbmSync.isResync();
		
		for (Items event : eventItems) {
			SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(event.getId());
			if (sbmGoogleSync == null) {
				String dateTime = event.getStart().getDateTime();

				if (dateTime == null) {
					String startDate = event.getStart().getDate();
					int providerId = Integer.valueOf(unitId[1]);
					Map<String, UnitWorkingTime> unitWorkingDayInfoMap = unitService.getUnitWorkDayInfo(companyLogin,
							endpoint, token, startDate, event.getEnd().getDate(), providerId);
					UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(startDate);
					if (unitWorkingTime != null) {

						Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
						WorkingTime workingTime = unitWorkingTimeMap.get(unitId[1]);
						SetWorkDayInfoInfoReq workDayInfoReq = new SetWorkDayInfoInfoReq(workingTime.getStart_time(),
								workingTime.getEnd_time(), null, 1, startDate, unitId[1], unitId[0]);
						SetWorkDayInfoReq workDayInfo = new SetWorkDayInfoReq(workDayInfoReq);
						boolean isBlocked = specialdayService.changeWorkDay(companyLogin, endpoint, token, workDayInfo);
						if (isBlocked) {
							UUID uuid = UUID.randomUUID();
							long bookingId = uuid.getMostSignificantBits();
							sbmGoogleSync = new SbmGoogleCalendar(bookingId, event.getId(), 1, GOOGLE,
									event.getOrganizer().getEmail(), event.getUpdated(), event.getStart().getDate(),
									event.getEnd().getDate(), event.getStart().getTimeZone(),
									event.getEnd().getTimeZone());
							sbmCalendarService.put(sbmGoogleSync);
						} else {
							m_log.info("Error during set work day info for provider with id "
									+ Integer.valueOf(unitId[1]));
						}
					}
				} else {
					addApptGroup(apptGroup, event);
				}
			} else {
				m_log.info("Event Id " + event + " is already created by TrueQuit");
				if (event.getStart().getDateTime() == null || event.getEnd().getDateTime() == null) {
					// TO-DO: will do this later
					m_log.info("Don't support modify for event has no start/end date time, ignoring: {}", event);
					continue;
				}
				if (sbmGoogleSync.getFlag() == 1) {
					if (GOOGLE.equals(sbmGoogleSync.getAgent())) {
						changeEventHandler.appointmentChanged(event, sbmGoogleSync, sbmId, dateToBeUpdated);
						if (isResync) {
							addApptGroup(apptGroup, event);
						}
					} else if (SBM.equalsIgnoreCase(sbmGoogleSync.getAgent())) {
						changeEventHandler.bookingChanged(event, sbmGoogleSync);
					}
				}
			}
		}
		if (!apptGroup.getAppts().isEmpty()) {
			changeBreakTime(apptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]), false);
			saveDb(apptGroup, false);
		}

		if (googleCalendarId != null && !dateToBeUpdated.isEmpty()) {
			try {
				m_log.info("Forcing to re-add all appointment of date {} after modifying an appointment",
						dateToBeUpdated);
				forceEventHandler.handle(googleCalendarId, dateToBeUpdated);
				m_log.info("Re-added all appointment successfully");
			} catch (Exception e) {
				m_log.error("{}", e);
			}
		}

		return true;
	}

	private void addApptGroup(PractitionerApptGroup apptGroup, Items event) {
		DateTimeZone dateTz = DateTimeZone.forID(CalendarSyncHandler.DEFAULT_TIME_ZONE);
		String convertedStartDateTime = TimeUtils.convertAndGetStartDateTimeGoogleEvent(event, dateTz);
		String convertedEndDateTime = TimeUtils.convertAndGetEndDateTimeGoogleEvent(event, dateTz);
		String date = TimeUtils.extractDate(convertedStartDateTime);
		m_log.info("convertedStartDateTime = {}, convertedEndDateTime = {}", convertedStartDateTime, convertedEndDateTime);
		apptGroup.addAppt(date,
				new GeneralAppt(convertedStartDateTime, convertedEndDateTime, event));
	}
	private void updateSbmGoogleCalendar(SbmGoogleCalendar sbmGoogleCalendar) {
		sbmCalendarService.put(sbmGoogleCalendar);
		m_log.info("Update to database successfully with value " + sbmGoogleCalendar);
	}

	private void changeBreakTime(PractitionerApptGroup group, String token, Integer unitId, Integer eventId,
			boolean isModified) throws SbmSDKException {

		Map<String, UnitWorkingTime> unitWorkingDayInfoMap = unitService.getUnitWorkDayInfo(
				env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(), token, group.getStartDateString(),
				group.getEndDateString(), unitId);
		UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(group.getStartDateString());
		Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
		WorkingTime workingTime = unitWorkingTimeMap.get(String.valueOf(unitId));
		Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = specialdayService.getWorkDaysInfo(
				env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
				new FromDate(group.getStartDateString(), workingTime.getStart_time()),
				new ToDate(group.getEndDateString(), workingTime.getEnd_time()));

		for (Entry<String, EventDateInfo> entry : group.getEventDateInfoMap().entrySet()) {
			Set<Breaktime> breakTimes = entry.getValue().breakTimeSet;
			String date = entry.getKey();

			if (!breakTimes.isEmpty()) {
				if (!isModified) {
					sbmBreakTimeManagement.addBreakTime(env.getSimplyBookCompanyLogin(),
							env.getSimplyBookAdminServiceUrl(), token, unitId, eventId, workingTime.getStart_time(),
							workingTime.getEnd_time(), date, breakTimes, workDayInfoMapForUnitId);
				} else {
					sbmBreakTimeManagement.removeBreakTime(env.getSimplyBookCompanyLogin(),
							env.getSimplyBookAdminServiceUrl(), token, unitId, eventId, workingTime.getStart_time(),
							workingTime.getEnd_time(), date, breakTimes, workDayInfoMapForUnitId);
				}
			}
		}
	}

	private void saveDb(PractitionerApptGroup group, boolean isModified) {
		m_log.info("Saving DB with isModified = " + isModified);
		for (Entry<String, EventDateInfo> entry : group.getEventDateInfoMap().entrySet()) {
			List<Items> googleEvents = entry.getValue().googleEvents;
			for (Items googleEvent : googleEvents) {
				if (!isModified) {
					UUID uuid = UUID.randomUUID();
					long bookingId = uuid.getMostSignificantBits();
					SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(googleEvent.getId());
					if (sbmGoogleSync == null) {
						sbmGoogleSync = new SbmGoogleCalendar(bookingId, googleEvent.getId(), 1, GOOGLE,
								googleEvent.getOrganizer().getEmail(), googleEvent.getUpdated(),
								googleEvent.getStart().getDateTime(), googleEvent.getEnd().getDateTime(),
								googleEvent.getStart().getTimeZone(), googleEvent.getEnd().getTimeZone());
						updateSbmGoogleCalendar(sbmGoogleSync);
					}
				} else {
					SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(googleEvent.getId());
					if (sbmGoogleSync != null) {
						sbmGoogleSync.setUpdated(googleEvent.getUpdated());
						sbmGoogleSync.setStartDateTime(googleEvent.getStart().getDateTime());
						sbmGoogleSync.setEndDateTime(googleEvent.getEnd().getDateTime());
						sbmGoogleSync.setStartTimeZone(googleEvent.getStart().getTimeZone());
						sbmGoogleSync.setEndTimeZone(googleEvent.getEnd().getTimeZone());
						updateSbmGoogleCalendar(sbmGoogleSync);
					}
				}
			}
		}
	}
}
