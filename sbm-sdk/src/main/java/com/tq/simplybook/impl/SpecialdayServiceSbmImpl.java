package com.tq.simplybook.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.WorkdayInfoReq;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.SetWorkDayInfoResp;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class SpecialdayServiceSbmImpl implements SpecialdayServiceSbm {
	  private static ObjectMapper JSON_MAPPER = new ObjectMapper();
	@Override
	public boolean blockTimeSlot(String companyLogin, String endpoint, String userToken, SetWorkDayInfoReq setWorkDayInfo)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					setWorkDayInfo);
			SetWorkDayInfoResp readValueForObject = SbmUtils.readValueForObject(jsonResp, SetWorkDayInfoResp.class);

			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during setWorkDayInfo()", e);
		}

	}

	@Override
	public boolean unlockTimeSlot(String companyLogin, String endpoint, String userToken, SetWorkDayInfoReq setWorkDayInfo)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					setWorkDayInfo);
			SetWorkDayInfoResp readValueForObject = SbmUtils.readValueForObject(jsonResp, SetWorkDayInfoResp.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during setWorkDayInfo()", e);
		}

	}

	@Override
	public WorksDayInfoResp getWorkDaysInfo(String companyLogin, String endpoint, String token, WorkdayInfoReq info) throws SbmSDKException {
		try {
			String jsonRes= SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getWorkDaysInfo", info);
			Map<String, Object> map = JSON_MAPPER.readValue(jsonRes, new TypeReference<Map<String, Object>>(){});
			return toWorksDayInfo(map);
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getWorkDayInfo()", e);
		}
	}
	
	private static WorksDayInfoResp toWorksDayInfo(Map<String, Object> map) {
		if(map != null) {
			Map<String, List<Map<String, String>>> result = (Map) map.get("result");
			if(result != null) {
				WorksDayInfoResp wdi = new WorksDayInfoResp();
				for(Entry<String, List<Map<String, String>>> dateEntry : result.entrySet()) {
					String date = dateEntry.getKey();
					List<Map<String, String>> timeInfoList = dateEntry.getValue();
					Set<WorkTimeSlot> timInfoSet = new HashSet<WorkTimeSlot>();
					for(Map<String, String> subTimeSlotMap : timeInfoList) {
						String from = subTimeSlotMap.get("from");
						String to = subTimeSlotMap.get("to");
						timInfoSet.add(new WorkTimeSlot(from, to));
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
