package com.tq.simplybook.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.TimeInfo;
import com.tq.simplybook.resp.WorkTimeDetail;
import com.tq.simplybook.resp.WorksDayInfo;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.test.WorkdayInfo;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class SpecialdayServiceSbmImpl implements SpecialdayServiceSbm {
	  private static ObjectMapper JSON_MAPPER = new ObjectMapper();
	@Override
	public boolean blockTimeSlot(String companyLogin, String endpoint, String userToken, SetWorkDayInfo setWorkDayInfo)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					setWorkDayInfo);
			WorkTimeDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, WorkTimeDetail.class);

			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during setWorkDayInfo()", e);
		}

	}

	@Override
	public boolean unlockTimeSlot(String companyLogin, String endpoint, String userToken, SetWorkDayInfo setWorkDayInfo)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					setWorkDayInfo);
			WorkTimeDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, WorkTimeDetail.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during setWorkDayInfo()", e);
		}

	}

	@Override
	public WorksDayInfo getWorkDaysInfo(String companyLogin, String endpoint, String token, WorkdayInfo info) throws SbmSDKException {
		try {
			String jsonRes= SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getWorkDaysInfo", info);
			Map<String, Object> map = JSON_MAPPER.readValue(jsonRes, new TypeReference<Map<String, Object>>(){});
			return toWorksDayInfo(map);
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getWorkDayInfo()", e);
		}
	}
	
	private static WorksDayInfo toWorksDayInfo(Map<String, Object> map) {
		if(map != null) {
			Map<String, List<Map<String, String>>> result = (Map) map.get("result");
			if(result != null) {
				WorksDayInfo wdi = new WorksDayInfo();
				for(Entry<String, List<Map<String, String>>> dateEntry : result.entrySet()) {
					String date = dateEntry.getKey();
					List<Map<String, String>> timeInfoList = dateEntry.getValue();
					Set<TimeInfo> timInfoSet = new HashSet<TimeInfo>();
					for(Map<String, String> subTimeSlotMap : timeInfoList) {
						String from = subTimeSlotMap.get("from");
						String to = subTimeSlotMap.get("to");
						timInfoSet.add(new TimeInfo(from, to));
					}
					
					wdi.setDate(date);
					wdi.setInfo(timInfoSet);
					return wdi;
				}
			}
			return null;
		} 
		
		return null;
	}


}
