package com.tq.simplybook.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetUnitListReq;
import com.tq.simplybook.req.WorkingDuration;
import com.tq.simplybook.resp.UnitInfoDetail;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class SbmUnitServiceImpl implements SbmUnitService {
	private static final Logger m_log = LoggerFactory.getLogger(SbmUnitServiceImpl.class);
	private static ObjectMapper JSON_MAPPER = new ObjectMapper();

	@Override
	public List<UnitProviderInfo> getUnitList(String companyLogin, String endpoint, String token, Boolean isVisibleOnly,
			Boolean asArray, Integer handleClasses) throws SbmSDKException {
		try {
			long start = System.currentTimeMillis();
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getUnitList",
					new GetUnitListReq(isVisibleOnly, asArray, handleClasses));
			m_log.info("Response from getUnitList method "+ jsonResp);
			 m_log.info("Get unit list take " + (System.currentTimeMillis() - start)+" ms");
			UnitInfoDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, UnitInfoDetail.class);

			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getUnitList()", e);
		}

	}

	@Override
	public Map<String, UnitWorkingTime> getUnitWorkDayInfo(String companyLogin, String endpoint, String token,
			String dateStart, String dateEnd, Integer unitGroupId) throws SbmSDKException {
		try {
			long start = System.currentTimeMillis();
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getUnitWorkdayInfo",
					new WorkingDuration(dateStart, dateEnd, unitGroupId));
			 m_log.info("Get unitWorkDayInfo " + (System.currentTimeMillis() - start)+" ms");
			Map<String, Object> map = JSON_MAPPER.readValue(jsonResp, new TypeReference<Map<String, Object>>() {
			});

			return toUnitWorkdayInfoMap(map);
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getUnitWorkdayInfo()", e);
		}
	}

	private Map<String, UnitWorkingTime> toUnitWorkdayInfoMap(Map<String, Object> map) {
		if (map != null) {
			Map<String, Map<String, Map<String, String>>> result = (Map) map.get("result");
			if (result != null) {
				Map<String, UnitWorkingTime> resMap = new LinkedHashMap<String, UnitWorkingTime>();
				Map<String, WorkingTime> unitMap = new LinkedHashMap<String, WorkingTime>();
				for (Entry<String, Map<String, Map<String, String>>> dateEntry : result.entrySet()) {
					String date = dateEntry.getKey();
					Map<String, Map<String, String>> unitWorkdayInfo = dateEntry.getValue();
					for (Entry<String, Map<String, String>> unitEntry : unitWorkdayInfo.entrySet()) {
						String unitId = unitEntry.getKey();
						Map<String, String> timeInfo = unitEntry.getValue();
						String startTime = timeInfo.get("start_time");
						String endTime = timeInfo.get("end_time");
						WorkingTime workingTime = new WorkingTime(startTime, endTime);
						unitMap.put(unitId, workingTime);
						UnitWorkingTime unitWorkingTime = new UnitWorkingTime(date, unitMap);
						resMap.put(date, unitWorkingTime);
					}
				}
				return resMap;
			}
			return Collections.emptyMap();
		}
		return null;

	}

}
