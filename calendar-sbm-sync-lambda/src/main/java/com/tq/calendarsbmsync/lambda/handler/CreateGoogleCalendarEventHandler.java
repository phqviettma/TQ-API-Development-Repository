package com.tq.calendarsbmsync.lambda.handler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.cliniko.lambda.model.GeneralAppt;
import com.tq.cliniko.lambda.model.PractitionerApptGroup;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.resp.Items;
import com.tq.simplybook.context.Env;
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

public class CreateGoogleCalendarEventHandler implements GoogleCalendarInternalHandler {
	private Env env = null;
	private TokenServiceSbm tokenService = null;
	private SpecialdayServiceSbm specialdayService = null;
	private SbmBreakTimeManagement sbmBreakTimeManagement = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private SbmUnitService unitService = null;
	private static final Logger m_log = LoggerFactory.getLogger(CreateGoogleCalendarEventHandler.class);

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
		String endpoint = env.getSimplyBookServiceUrlLogin();
		String password = env.getSimplyBookPassword();
		String username = env.getSimplyBookUser();
		String unitId[] = sbmId.split("-");
		String token = tokenService.getUserToken(companyLogin, username, password, endpoint);
		PractitionerApptGroup apptGroup = new PractitionerApptGroup();
		for (Items event : eventItems) {
			SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(event.getId());
			if (sbmGoogleSync == null) {
				String date = UtcTimeUtil.extractDate(event.getStart().getDateTime());

				apptGroup.addAppt(date, new GeneralAppt(event.getStart().getDateTime(), event.getEnd().getDateTime()));
			} else {
				m_log.info("Event Id " + event + " is aldready created by TrueQuit, ignoring");
			}
		}

		addBreakTime(apptGroup, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]));

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

		for (Entry<String, Set<Breaktime>> dateToSbmBreakTime : group.getDateToSbmBreakTimesMap().entrySet()) {
			Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue();
			String date = dateToSbmBreakTime.getKey();
			if (!breakTimes.isEmpty()) {
				sbmBreakTimeManagement.addBreakTime(env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(),
						token, unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date,
						breakTimes, workDayInfoMapForUnitId);
			}
		}
	}

}
