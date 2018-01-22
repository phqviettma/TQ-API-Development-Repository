package com.tq.calendarsbmsync.lambda.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import com.tq.calendar.resp.Items;
import com.tq.cliniko.lambda.model.GeneralAppt;
import com.tq.cliniko.lambda.model.PractitionerApptGroup;
import com.tq.cliniko.time.UtcTimeUtil;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateGoogleCalendarEventHandler implements GoogleCalendarInternalHandler {
	private Env enV = null;
	private TokenServiceSbm tokenService = null;
	private SpecialdayServiceSbm specialdayService = null;
	private SbmBreakTimeManagement sbmBreakTimeManagement = null;
	private SbmGoogleCalendarDbService sbmCalendarService = null;
	private static final Logger m_log = LoggerFactory.getLogger(CreateGoogleCalendarEventHandler.class);

	public CreateGoogleCalendarEventHandler(Env env, TokenServiceSbm tss, SpecialdayServiceSbm sds,
			SbmBreakTimeManagement sbt, SbmGoogleCalendarDbService sdcs) {
		enV = env;
		tokenService = tss;
		specialdayService = sds;
		sbmBreakTimeManagement = sbt;
		sbmCalendarService = sdcs;
	}

	@Override
	public void handle(List<Items> item, String sbmId) throws SbmSDKException {
		syncToSbm(item, sbmId);
	}

	private boolean syncToSbm(List<Items> eventItems, String sbmId) throws SbmSDKException {
		String companyLogin = enV.getSimplyBookCompanyLogin();
		String endpoint = enV.getSimplyBookServiceUrlLogin();
		String password = enV.getSimplyBookPassword();
		String username = enV.getSimplyBookUser();
		String unitId[] = sbmId.split("-");
		String token = tokenService.getUserToken(companyLogin, username, password, endpoint);
		Map<String, PractitionerApptGroup> apptGroupMap = new HashMap<String, PractitionerApptGroup>();
		for (Items event : eventItems) {
			SbmGoogleCalendar sbmGoogleSync = sbmCalendarService.queryWithIndex(event.getId());
			if (sbmGoogleSync == null) {
				String date = UtcTimeUtil.extractDate(event.getStart().getDateTime());
				PractitionerApptGroup group = apptGroupMap.get(date);
				if (group == null) {
					group = new PractitionerApptGroup();
					apptGroupMap.put(date, group);
				}
				group.addAppt(date, new GeneralAppt(event.getStart().getDateTime(), event.getEnd().getDateTime()));
			} else {
				m_log.info("Event Id " + event + " is aldready created by TrueQuit, ignoring");
			}
		}

		addBreakTime(apptGroupMap, token, Integer.valueOf(unitId[1]), Integer.valueOf(unitId[0]));

		return true;
	}

	private void addBreakTime(Map<String, PractitionerApptGroup> apptGroup, String token, Integer unitId,
			Integer eventId) throws SbmSDKException {
		for (Entry<String, PractitionerApptGroup> entry : apptGroup.entrySet()) {
			PractitionerApptGroup group = entry.getValue();
			Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = specialdayService.getWorkDaysInfo(
					enV.getSimplyBookCompanyLogin(), enV.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
					new FromDate(group.getStartDateString(), enV.getSimplybookWorkingStartTime()),
					new ToDate(group.getEndDateString(), enV.getSimplybookWorkingEndTime()));

			for (Entry<String, Set<Breaktime>> dateToSbmBreakTime : group.getDateToSbmBreakTimesMap().entrySet()) {
				Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue();
				String date = dateToSbmBreakTime.getKey();
				if (!breakTimes.isEmpty()) {
					sbmBreakTimeManagement.addBreakTime(enV.getSimplyBookCompanyLogin(),
							enV.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
							enV.getSimplybookWorkingStartTime(), enV.getSimplybookWorkingEndTime(), date, breakTimes,
							workDayInfoMapForUnitId);
				}
			}
		}
	}

}
