package com.tq.gcsyncsbm.lambda.handler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.model.GeneralAppt;
import com.tq.googlecalendar.model.PractitionerApptGroup;
import com.tq.googlecalendar.model.PractitionerApptGroup.EventDateInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.time.TimeUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.EditBookReq;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.utils.SbmUtils;

public class ChangeGoogleEventHandler {
	private static final Logger m_logger = LoggerFactory.getLogger(ChangeGoogleEventHandler.class);
	private static final String GOOGLE = "google";
	private Env m_env = null;
	private SbmGoogleCalendarDbService m_sbmCalendarService = null;
	private BookingServiceSbm m_bookingService = null;
	private TokenServiceSbm m_tokenService = null;
	private SbmUnitService m_unitService = null;
	private SpecialdayServiceSbm m_specialdayService = null;
	private SbmBreakTimeManagement m_sbmBreakTimeManagement = null;

	public ChangeGoogleEventHandler(Env env, BookingServiceSbm bookingService,
			SbmGoogleCalendarDbService sbmCalendarService, TokenServiceSbm tokenService, SbmUnitService unitService,
			SpecialdayServiceSbm specialdayService, SbmBreakTimeManagement sbmBreakTimeManagement) {
		m_env = env;
		m_bookingService = bookingService;
		m_sbmCalendarService = sbmCalendarService;
		m_tokenService = tokenService;
		m_unitService = unitService;
		m_specialdayService = specialdayService;
		m_sbmBreakTimeManagement = sbmBreakTimeManagement;
	}

	public void bookingChanged(Items event, SbmGoogleCalendar sbmGoogleSync) throws SbmSDKException {
		String companyLogin = m_env.getSimplyBookCompanyLogin();
		String endpoint = m_env.getSimplyBookAdminServiceUrl();

		String newUpdatedAt = event.getUpdated();
		String oldUpdatedAt = sbmGoogleSync.getUpdated();
		if (oldUpdatedAt != null && oldUpdatedAt.equals(newUpdatedAt)) {
			m_logger.info("Ignoring appointment id {} due to no difference updated time or booked first time.",
					sbmGoogleSync.getSbmId());
			sbmGoogleSync.setUpdated(event.getUpdated());
			updateSbmGoogleCalendar(sbmGoogleSync);
			return;
		}

		m_logger.info("Event Id " + event.getId()
				+ " is already updated by Google Calendar. Handle syncing to SBM (agent = SBM)");
		String token = getUserToken();
		BookingInfo bookingInfo = m_bookingService.getBookingInfo(companyLogin, endpoint, token,
				sbmGoogleSync.getSbmId());

		DateTimeZone dateTz = DateTimeZone.forID(CalendarSyncHandler.DEFAULT_TIME_ZONE);
		String newStartDateTime = TimeUtils.convertAndGetStartDateTimeGoogleEvent(event, dateTz);
		String newEndDateTime = TimeUtils.convertAndGetEndDateTimeGoogleEvent(event, dateTz);

		String newStartDate = TimeUtils.extractDate(newStartDateTime);
		String newEndDate = TimeUtils.extractDate(newEndDateTime);
		String newStartTime = TimeUtils.extractTimeHMS(newStartDateTime);
		String newEndTime = TimeUtils.extractTimeHMS(newEndDateTime);

		if (SbmUtils.compareCurrentDateTimeToNewDateTime(bookingInfo, newStartDate + " " + newStartTime,
				newEndDate + " " + newEndTime)) {
			m_logger.info("There is no change which related to the date or time. Ignore this");
			sbmGoogleSync.setUpdated(event.getUpdated());
			updateSbmGoogleCalendar(sbmGoogleSync);
			return;
		}

		EditBookReq editBookReq = new EditBookReq(bookingInfo, newStartDate, newStartTime, newEndDate, newEndTime);

		boolean result = m_bookingService.editBooking(companyLogin, endpoint, token, editBookReq);
		if (result) {
			m_logger.info("Event Id {} is synced to SBM success", event.getId());
			sbmGoogleSync.setUpdated(event.getUpdated());
			sbmGoogleSync.setStartDateTime(event.getStart().getDateTime());
			sbmGoogleSync.setEndDateTime(event.getEnd().getDateTime());
			sbmGoogleSync.setStartTimeZone(event.getStart().getTimeZone());
			sbmGoogleSync.setEndTimeZone(event.getEnd().getTimeZone());
			updateSbmGoogleCalendar(sbmGoogleSync);
		} else {
			m_logger.error("Event Id {} is not synced to SBM", event.getId());
		}
	}

	public void appointmentChanged(Items event, SbmGoogleCalendar sbmGoogleSync, String sbmId, Set<String> dateToBeUpdated) throws SbmSDKException {
		String unitId[] = sbmId.split("-");
		String newUpdatedAt = event.getUpdated();
		if (!newUpdatedAt.equals(sbmGoogleSync.getUpdated())) {
			m_logger.info("Event Id " + event.getId() + " is already updated by Google Calendar. Handle syncing to SBM (agent = google)");
			PractitionerApptGroup removeApptGroup = new PractitionerApptGroup();
			if (sbmGoogleSync.getStartDateTime() == null) {
				// Some appointment dont have startDatetime
				// before this fix
				m_logger.warn("appointment id {} doesn't have startDateTime (before this fix)",
						event.getId());
				return;
			}
			String token = getUserToken();
			DateTimeZone dateTz = DateTimeZone.forID(CalendarSyncHandler.DEFAULT_TIME_ZONE);
			String oldStartDateTime = TimeUtils.convertAndGetStartDateTimeGoogleEvent(sbmGoogleSync, dateTz);
			String oldEndDateTime = TimeUtils.convertAndGetEndDateTimeGoogleEvent(sbmGoogleSync, dateTz);
			String date = TimeUtils.extractDate(oldStartDateTime);
			removeApptGroup.addAppt(date, new GeneralAppt(oldStartDateTime,
					oldEndDateTime, event));
			changeBreakTime(removeApptGroup, token, Integer.valueOf(unitId[1]),
					Integer.valueOf(unitId[0]), true);
			
			
			String newStartDateTime = TimeUtils.convertAndGetStartDateTimeGoogleEvent(event, dateTz);
			String newEndDateTime = TimeUtils.convertAndGetEndDateTimeGoogleEvent(event, dateTz);
			m_logger.info("convertedStartDateTime = {}, convertedEndDateTime = {}", newStartDateTime, newEndDateTime);
			date = TimeUtils.extractDate(newStartDateTime);
			PractitionerApptGroup updateApptGroup = new PractitionerApptGroup();
			updateApptGroup.addAppt(date, new GeneralAppt(newStartDateTime,
					newEndDateTime, event));
			changeBreakTime(updateApptGroup, token, Integer.valueOf(unitId[1]),
					Integer.valueOf(unitId[0]), false);
			saveDb(updateApptGroup, true);
			m_logger.info("Event Id " + event.getId() + " is synced to SBM");
			dateToBeUpdated.add(date);
		}
	}

	private void updateSbmGoogleCalendar(SbmGoogleCalendar sbmGoogleCalendar) {
		m_sbmCalendarService.put(sbmGoogleCalendar);
		m_logger.info("Update to database successfully with value " + sbmGoogleCalendar);
	}

	private String getUserToken() throws SbmSDKException {
		String companyLogin = m_env.getSimplyBookCompanyLogin();
		String endpointLogin = m_env.getSimplyBookServiceUrlLogin();
		String password = m_env.getSimplyBookPassword();
		String username = m_env.getSimplyBookUser();
		return m_tokenService.getUserToken(companyLogin, username, password, endpointLogin);
	}

	private void changeBreakTime(PractitionerApptGroup group, String token, Integer unitId, Integer eventId,
			boolean isModified) throws SbmSDKException {

		Map<String, UnitWorkingTime> unitWorkingDayInfoMap = m_unitService.getUnitWorkDayInfo(
				m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(), token, group.getStartDateString(),
				group.getEndDateString(), unitId);
		UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(group.getStartDateString());
		Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
		WorkingTime workingTime = unitWorkingTimeMap.get(String.valueOf(unitId));
		Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = m_specialdayService.getWorkDaysInfo(
				m_env.getSimplyBookCompanyLogin(), m_env.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
				new FromDate(group.getStartDateString(), workingTime.getStart_time()),
				new ToDate(group.getEndDateString(), workingTime.getEnd_time()));

		for (Entry<String, EventDateInfo> entry : group.getEventDateInfoMap().entrySet()) {
			Set<Breaktime> breakTimes = entry.getValue().breakTimeSet;
			String date = entry.getKey();

			if (!breakTimes.isEmpty()) {
				if (!isModified) {
					m_sbmBreakTimeManagement.addBreakTime(m_env.getSimplyBookCompanyLogin(),
							m_env.getSimplyBookAdminServiceUrl(), token, unitId, eventId, workingTime.getStart_time(),
							workingTime.getEnd_time(), date, breakTimes, workDayInfoMapForUnitId);
				} else {
					m_sbmBreakTimeManagement.removeBreakTime(m_env.getSimplyBookCompanyLogin(),
							m_env.getSimplyBookAdminServiceUrl(), token, unitId, eventId, workingTime.getStart_time(),
							workingTime.getEnd_time(), date, breakTimes, workDayInfoMapForUnitId);
				}
			}
		}
	}
	
	private void saveDb(PractitionerApptGroup group, boolean isModified) {
		m_logger.info("Saving DB with isModified = " + isModified);
		for (Entry<String, EventDateInfo> entry : group.getEventDateInfoMap().entrySet()) {
			List<Items> googleEvents = entry.getValue().googleEvents;
			for (Items googleEvent : googleEvents) {
				if (!isModified) {
					UUID uuid = UUID.randomUUID();
					long bookingId = uuid.getMostSignificantBits();
					SbmGoogleCalendar sbmGoogleSync = new SbmGoogleCalendar(bookingId, googleEvent.getId(), 1, GOOGLE,
							googleEvent.getOrganizer().getEmail(), googleEvent.getUpdated(),
							googleEvent.getStart().getDateTime(), googleEvent.getEnd().getDateTime(),
							googleEvent.getStart().getTimeZone(), googleEvent.getEnd().getTimeZone());
					updateSbmGoogleCalendar(sbmGoogleSync);
				} else {
					SbmGoogleCalendar sbmGoogleSync = m_sbmCalendarService.queryWithIndex(googleEvent.getId());
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
