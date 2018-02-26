package com.tq.simplybook.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.req.WorkdayInfoReq;
import com.tq.simplybook.resp.SetWorkDayInfoResp;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class SpecialdayServiceSbmImpl implements SpecialdayServiceSbm {

	private static ObjectMapper JSON_MAPPER = new ObjectMapper();
	private static Logger m_log = LoggerFactory.getLogger(SpecialdayServiceSbmImpl.class);

	@Override
	public boolean changeWorkDay(String companyLogin, String endpoint, String userToken,
			SetWorkDayInfoReq setWorkDayInfo) throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					setWorkDayInfo);
			SetWorkDayInfoResp readValueForObject = SbmUtils.readValueForObject(jsonResp, SetWorkDayInfoResp.class);
			
			return readValueForObject.getResult();
		} catch (Exception e) {
			m_log.info("Error when setWorkDayInfo" + e);
			return false;
			
		}

	}

	@Override
	public Map<String, WorksDayInfoResp> getWorkDaysInfo(String companyLogin, String endpoint, String token,
			int unit_id, int event_id, FromDate fromDate, ToDate toDate) throws SbmSDKException {
		return getWorkDaysInfo(companyLogin, endpoint, token,
				new WorkdayInfoReq(fromDate.toString(), toDate.toString(), unit_id, event_id));
	}

	private Map<String, WorksDayInfoResp> getWorkDaysInfo(String companyLogin, String endpoint, String token,
			WorkdayInfoReq info) throws SbmSDKException {
		try {
			String jsonRes = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getWorkDaysInfo", info);
			m_log.info("getWorkDaysInfo response: " + jsonRes);
			Map<String, Object> map = JSON_MAPPER.readValue(jsonRes, new TypeReference<Map<String, Object>>() {
			});
			return toWorksDayInfoResp(map);
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getWorkDayInfo()", e);
		}
	}

	private static Map<String, WorksDayInfoResp> toWorksDayInfoResp(Map<String, Object> map) {
		if (map != null) {
			Map<String, List<Map<String, String>>> result = (Map) map.get("result");
			if (result != null) {
				Map<String, WorksDayInfoResp> resMap = new LinkedHashMap<String, WorksDayInfoResp>();
				for (Entry<String, List<Map<String, String>>> dateEntry : result.entrySet()) {
					WorksDayInfoResp wdi = new WorksDayInfoResp();
					String date = dateEntry.getKey();
					List<Map<String, String>> timeInfoList = dateEntry.getValue();
					Set<WorkTimeSlot> workTimeSlotSet = new HashSet<WorkTimeSlot>();
					for (Map<String, String> subTimeSlotMap : timeInfoList) {
						String from = subTimeSlotMap.get("from");
						String to = subTimeSlotMap.get("to");
						workTimeSlotSet.add(new WorkTimeSlot(from, to));
					}

					wdi.setDate(date);
					wdi.setInfo(workTimeSlotSet);
					resMap.put(date, wdi);
				}

				return resMap;
			}
			return Collections.emptyMap();
		}

		return null;
	}

}
