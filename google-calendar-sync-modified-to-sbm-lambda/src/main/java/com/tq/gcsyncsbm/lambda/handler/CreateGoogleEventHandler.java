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
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.EditBookReq;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
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

public class CreateGoogleEventHandler implements GCInternalHandler {
	private static final Logger m_log = LoggerFactory.getLogger(CreateGoogleEventHandler.class);
	private Env env = null;
	private TokenServiceSbm tokenService = null;
	private SpecialdayServiceSbm specialdayService = null;
	private SbmBreakTimeManagement sbmBreakTimeManagement = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private SbmUnitService unitService = null;
	private BookingServiceSbm bookingService = null;
	private static final String AGENT = "google";
	private static final String SBM = "sbm";
	private static final String DEFAULT_TIME_ZONE = "Australia/Sydney";

	public CreateGoogleEventHandler(Env env, TokenServiceSbm tss, SpecialdayServiceSbm sds, SbmBreakTimeManagement sbt,
			SbmGoogleCalendarDbService sdcs, SbmUnitService uss, BookingServiceSbm bookingService) {
		this.env = env;
		this.tokenService = tss;
		this.specialdayService = sds;
		this.sbmBreakTimeManagement = sbt;
		this.sbmCalendarService = sdcs;
		this.unitService = uss;
		this.bookingService = bookingService;
	}

	@Override
	public void handle(List<Items> item, String sbmId) throws SbmSDKException, InfSDKExecption {
		syncToSbm(item, sbmId);
	}

	private boolean syncToSbm(List<Items> eventItems, String sbmId) throws SbmSDKException {
		String companyLogin = env.getSimplyBookCompanyLogin();
		String endpointLogin = env.getSimplyBookServiceUrlLogin();
		String endpoint = env.getSimplyBookAdminServiceUrl();
		String password = env.getSimplyBookPassword();
		String username = env.getSimplyBookUser();
		String unitId[] = sbmId.split("-");

		String token = tokenService.getUserToken(companyLogin, username, password, endpointLogin);
		PractitionerApptGroup apptGroup = new PractitionerApptGroup();
		for (Items event : eventItems) {
			if (event.getStart().getDateTime() == null || event.getEnd().getDateTime() == null) {
				m_log.info("There are no date time from start date time and end date time, ignoring");
				continue;
			}
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
							sbmGoogleSync = new SbmGoogleCalendar(bookingId, event.getId(), 1, AGENT,
									event.getOrganizer().getEmail(), event.getUpdated(), event.getStart().getDateTime(), event.getEnd().getDateTime());
							sbmCalendarService.put(sbmGoogleSync);
						} else {
							m_log.info("Error during set work day info for provider with id "
									+ Integer.valueOf(unitId[1]));
						}
					}
				} else {
					String date = TimeUtils.extractDate(event.getStart().getDateTime());
					apptGroup.addAppt(date,
							new GeneralAppt(event.getStart().getDateTime(), event.getEnd().getDateTime(), event));
				}
			} else {
				m_log.info("Event Id " + event + " is already created by TrueQuit");
				if (sbmGoogleSync.getFlag() == 1) {
    				if (AGENT.equals(sbmGoogleSync.getAgent())) {
    					String newUpdatedAt = event.getUpdated();
    					if (!newUpdatedAt.equals(sbmGoogleSync.getUpdated())) {
    						m_log.info("Event Id " + event + " is updated by Google Calendar");
    						PractitionerApptGroup removeApptGroup = new PractitionerApptGroup();
    						if (sbmGoogleSync.getStartDateTime() == null) {
    							// Some appointment dont have startDatetime before this fix
    							m_log.warn("appointment id {} doesn't have startDateTime (before this fix)", event.getId());
    							continue;
    						}
    						String date = TimeUtils.extractDate(sbmGoogleSync.getStartDateTime());
    						removeApptGroup.addAppt(date,
    								new GeneralAppt(sbmGoogleSync.getStartDateTime(), sbmGoogleSync.getEndDateTime(), event));
    						changeBreakTime(removeApptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]), true);
    						
    						date = TimeUtils.extractDate(event.getStart().getDateTime());
    						PractitionerApptGroup updateApptGroup = new PractitionerApptGroup();
    						updateApptGroup.addAppt(date, 
    								new GeneralAppt(event.getStart().getDateTime(), event.getEnd().getDateTime(), event));
    						changeBreakTime(updateApptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]), false);
    						saveDb(updateApptGroup, true);
    						m_log.info("Event Id "+ event.getId() + " is synced to SBM");
    					}
    				} else if (SBM.equalsIgnoreCase(sbmGoogleSync.getAgent())) {
    					String newUpdatedAt = event.getUpdated();
    					String oldUpdatedAt = sbmGoogleSync.getUpdated();
    					if (oldUpdatedAt != null && oldUpdatedAt.equals(newUpdatedAt)) {
    						m_log.info("Ignoring appointment id {} due to no difference updated time or booked first time.", sbmGoogleSync.getSbmId());
    						sbmGoogleSync.setUpdated(event.getUpdated());
    						updateSbmGoogleCalendar(sbmGoogleSync);
    						continue;
    					}
    					
    					m_log.info("Event Id "+event.getId()+" is updated. Handle syncing to SBM (agent = SBM)");
    					
    					BookingInfo bookingInfo = bookingService.getBookingInfo(companyLogin, endpoint, token, sbmGoogleSync.getSbmId());
    					
    					DateTimeZone dateTz = DateTimeZone.forID(DEFAULT_TIME_ZONE);
    					String newStartDateTime = TimeUtils.convertToTzFromLondonTz(dateTz, event.getStart().getDateTime());
    					String newEndDateTime = TimeUtils.convertToTzFromLondonTz(dateTz, event.getEnd().getDateTime());
    					
    					String newStartDate = TimeUtils.extractDate(newStartDateTime);
    					String newEndDate = TimeUtils.extractDate(newEndDateTime);
    					String newStartTime = TimeUtils.extractTimeHMS(newStartDateTime);
    					String newEndTime = TimeUtils.extractTimeHMS(newEndDateTime);
    					
    					if (SbmUtils.compareCurrentDateTimeToNewDateTime(bookingInfo, newStartDate + " " + newStartTime, newEndDate + " " + newEndTime)) {
    						m_log.info("There is no change which related to the date or time. Ignore this");
    						sbmGoogleSync.setUpdated(event.getUpdated());
    						updateSbmGoogleCalendar(sbmGoogleSync);
    						continue;
    					}
    					
    					EditBookReq editBookReq = new EditBookReq(bookingInfo, newStartDate, newStartTime, newEndDate, newEndTime);
    					
    					boolean result = bookingService.editBooking(companyLogin, endpoint, token, editBookReq);
    					if (result) {
    						m_log.info("Event Id {} is synced to SBM success", event.getId());
    						sbmGoogleSync.setUpdated(event.getUpdated());
    						updateSbmGoogleCalendar(sbmGoogleSync);
    					} else {
    						m_log.error("Event Id {} is not synced to SBM", event.getId());
    					}
    					
    				}
				}
			}
		}
		if (!apptGroup.getAppts().isEmpty()) {
			changeBreakTime(apptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]), false);
			saveDb(apptGroup, false);
		}

		return true;
	}

	private void updateSbmGoogleCalendar(SbmGoogleCalendar sbmGoogleCalendar) {
		sbmCalendarService.put(sbmGoogleCalendar);
		m_log.info("Update to database successfully with value " +sbmGoogleCalendar);
	}
	private void changeBreakTime(PractitionerApptGroup group, String token, Integer unitId, Integer eventId, boolean isModified)
			throws SbmSDKException {

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
    				sbmBreakTimeManagement.addBreakTime(env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(),
    						token, unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date,
    						breakTimes, workDayInfoMapForUnitId);
				} else {
					sbmBreakTimeManagement.removeBreakTime(env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(),
    						token, unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date,
    						breakTimes, workDayInfoMapForUnitId);
				}
			}
		}
	}
	
	private void saveDb(PractitionerApptGroup group, boolean isModified) {
		m_log.info("Saving DB with isModified = "+isModified);
		for (Entry<String, EventDateInfo> entry : group.getEventDateInfoMap().entrySet()) {
    		List<Items> googleEvents = entry.getValue().googleEvents;
    		for (Items googleEvent : googleEvents) {
    			if (!isModified) {
    				UUID uuid = UUID.randomUUID();
    				long bookingId = uuid.getMostSignificantBits();
    				SbmGoogleCalendar sbmGoogleSync = new SbmGoogleCalendar(bookingId, googleEvent.getId(), 1, AGENT,
    						googleEvent.getOrganizer().getEmail(), googleEvent.getUpdated(), googleEvent.getStart().getDateTime(), googleEvent.getEnd().getDateTime());
    				updateSbmGoogleCalendar(sbmGoogleSync);
    			} else {
    				SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(googleEvent.getId());
    				if (sbmGoogleSync != null) {
    					sbmGoogleSync.setUpdated(googleEvent.getUpdated());
    					updateSbmGoogleCalendar(sbmGoogleSync);
    				}
    			}
    		}
		}
	}
}
