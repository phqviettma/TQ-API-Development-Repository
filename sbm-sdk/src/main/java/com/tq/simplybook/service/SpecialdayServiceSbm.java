package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.WorksDayInfo;
import com.tq.simplybook.test.WorkdayInfo;

public interface SpecialdayServiceSbm {
	boolean blockTimeSlot(String companyLogin, String endpoint, String token, SetWorkDayInfo info)
			throws SbmSDKException;

	boolean unlockTimeSlot(String companyLogin, String endpoint, String token, SetWorkDayInfo info)
			throws SbmSDKException;
	
	WorksDayInfo getWorkDaysInfo(String companyLogin, String endpoint, String token, WorkdayInfo info) throws SbmSDKException;
	
}
