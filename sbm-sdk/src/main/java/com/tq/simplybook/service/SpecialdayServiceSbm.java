package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.DayInfo;

public interface SpecialdayServiceSbm {
	boolean blockTimeSlot(String companyLogin, String endpoint, String token, SetWorkDayInfo info)
			throws SbmSDKException;

	boolean unlockTimeSlot(String companyLogin, String endpoint, String token, SetWorkDayInfo info)
			throws SbmSDKException;
	
	DayInfo getWorkDaysInfo(String companyLogin, String endpoint, String token);
	
}
