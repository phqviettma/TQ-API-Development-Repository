package com.tq.calendarsbmsync.lambda.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.model.GeneralAppt;
import com.tq.googlecalendar.model.PractitionerApptGroup;
import com.tq.googlecalendar.model.PractitionerApptGroup.EventDateInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.simplybook.context.Env;
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

public class CreateGoogleCalendarEventHandler implements GoogleCalendarInternalHandler {
	private static final Logger m_log = LoggerFactory.getLogger(CreateGoogleCalendarEventHandler.class);
	private Env env = null;
	private TokenServiceSbm tokenService = null;
	private SpecialdayServiceSbm specialdayService = null;
	private SbmBreakTimeManagement sbmBreakTimeManagement = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private SbmUnitService unitService = null;

	public CreateGoogleCalendarEventHandler(Env env, TokenServiceSbm tss, SpecialdayServiceSbm sds,
			SbmBreakTimeManagement sbt, SbmGoogleCalendarDbService sdcs, SbmUnitService uss) {
		this.env = env;
		this.tokenService = tss;
		this.specialdayService = sds;
		this.sbmBreakTimeManagement = sbt;
		this.sbmCalendarService = sdcs;
		this.unitService = uss;
	}

	@Override
	public void handle(List<Items> item, String sbmId) throws SbmSDKException {
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
						Set<Breaktime> breakTimes = new HashSet<>();
						Breaktime breakTime = new Breaktime(workingTime.getStart_time(), workingTime.getEnd_time());
						breakTimes.add(breakTime);
						SetWorkDayInfoInfoReq workDayInfoReq = new SetWorkDayInfoInfoReq(workingTime.getStart_time(),
								workingTime.getEnd_time(), breakTimes, startDate, unitId[1], unitId[0]);
						SetWorkDayInfoReq workDayInfo = new SetWorkDayInfoReq(workDayInfoReq);
						boolean isBlocked = specialdayService.changeWorkDay(companyLogin, endpoint, token, workDayInfo);
						if (isBlocked) {
							UUID uuid = UUID.randomUUID();
							long bookingId = uuid.getMostSignificantBits();
							sbmGoogleSync = new SbmGoogleCalendar(bookingId, event.getId(), "-BLANK-", 1, "google");
							sbmCalendarService.put(sbmGoogleSync);
						} else {
							m_log.info("Error during set work day info for provider with id "
									+ Integer.valueOf(unitId[1]));
						}
					}
				} else {
					String date = UtcTimeUtil.extractDate(event.getStart().getDateTime());
					apptGroup.addAppt(date,
							new GeneralAppt(event.getStart().getDateTime(), event.getEnd().getDateTime(),event.getId(), sbmGoogleSync));
					addBreakTime(apptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]));
				}
			} else {
				m_log.info("Event Id " + event + " is aldready created by TrueQuit, ignoring");
			}
		}
		if (!apptGroup.getAppts().isEmpty()) {
			addBreakTime(apptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]));
			
		}


		return true;
	}

	private void addBreakTime(PractitionerApptGroup group, String token, Integer unitId, Integer eventId)
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

		for (Entry<String, EventDateInfo> dateToSbmBreakTime :  group.getEventDateInfoMap().entrySet()) {
			Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue().breakTimeSet;
			String date = dateToSbmBreakTime.getKey();
			if (!breakTimes.isEmpty()) {
				sbmBreakTimeManagement.addBreakTime(env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(),
						token, unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date,
						breakTimes, workDayInfoMapForUnitId);
			}
			List<String> geventIdList = dateToSbmBreakTime.getValue().geventIdList;
			long start = System.currentTimeMillis();

			for (String gevent : geventIdList) {
				UUID uuid = UUID.randomUUID();
				long bookingId = uuid.getMostSignificantBits();
				SbmGoogleCalendar sbmGoogleSync = new SbmGoogleCalendar(bookingId, gevent, "-BLANK-", 1, "google");
				sbmCalendarService.put(sbmGoogleSync);
			}

			m_log.info("Save to database take " + (System.currentTimeMillis() - start) + " ms");
		}
	}

}
