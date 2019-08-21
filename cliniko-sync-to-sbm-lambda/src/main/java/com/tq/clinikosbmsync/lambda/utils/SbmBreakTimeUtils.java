package com.tq.clinikosbmsync.lambda.utils;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.tq.cliniko.lambda.model.PractitionerApptGroup;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
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

public class SbmBreakTimeUtils {
	public static void changeSbmBreakTime(Env env, SbmUnitService unitService, SbmBreakTimeManagement m_sbtm,
			SpecialdayServiceSbm m_sss, PractitionerApptGroup group, String token, boolean isAdditional,
			ClinikoSbmSync clinikoSbmSync) throws SbmSDKException {
		String sbmId[] = clinikoSbmSync.getSbmId().split("-");
		Integer unitId = Integer.valueOf(sbmId[1]);
		Integer eventId = Integer.valueOf(sbmId[0]);
		Map<String, UnitWorkingTime> unitWorkingDayInfoMap = unitService.getUnitWorkDayInfo(
				env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(), token, group.getStartDateString(),
				group.getEndDateString(), unitId);
		UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get(group.getStartDateString());
		Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
		WorkingTime workingTime = unitWorkingTimeMap.get(String.valueOf(unitId));
		Map<String, WorksDayInfoResp> workDayInfoMapForUnitId = m_sss.getWorkDaysInfo(env.getSimplyBookCompanyLogin(),
				env.getSimplyBookAdminServiceUrl(), token, unitId, eventId,
				new FromDate(group.getStartDateString(), workingTime.getStart_time()),
				new ToDate(group.getEndDateString(), workingTime.getEnd_time()));

		for (Entry<String, Set<Breaktime>> dateToSbmBreakTime : group.getDateToSbmBreakTimesMap().entrySet()) {
			Set<Breaktime> breakTimes = dateToSbmBreakTime.getValue();
			String date = dateToSbmBreakTime.getKey();
			if (!breakTimes.isEmpty()) {
				if (isAdditional) {
					m_sbtm.addBreakTime(env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(), token,
							unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date, breakTimes,
							workDayInfoMapForUnitId);
				} else {
					m_sbtm.removeBreakTime(env.getSimplyBookCompanyLogin(), env.getSimplyBookAdminServiceUrl(), token,
							unitId, eventId, workingTime.getStart_time(), workingTime.getEnd_time(), date, breakTimes,
							workDayInfoMapForUnitId);
				}
			}

		}

	}
}
