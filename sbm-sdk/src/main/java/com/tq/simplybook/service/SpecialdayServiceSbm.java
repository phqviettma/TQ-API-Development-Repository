package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.WorkdayInfoReq;
import com.tq.simplybook.resp.WorksDayInfoResp;

public interface SpecialdayServiceSbm {
	boolean blockTimeSlot(String companyLogin, String endpoint, String token, SetWorkDayInfoReq info)
			throws SbmSDKException;

	boolean unlockTimeSlot(String companyLogin, String endpoint, String token, SetWorkDayInfoReq info)
			throws SbmSDKException;
	
	WorksDayInfoResp getWorkDaysInfo(String companyLogin, String endpoint, String token, WorkdayInfoReq info) throws SbmSDKException;
	
}
