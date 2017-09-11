package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.DayInfoReq;
import com.tq.simplybook.req.SpecialDayReq;
import com.tq.simplybook.test.WorkdayInfo;

public interface SpecialdayServiceSbm {
	public boolean blockTimeSlot(String companyLogin, String endpoint, String token, DayInfoReq info)
			throws SbmSDKException;

	public boolean unlockTimeSlot(String companyLogin, String endpoint, String token, DayInfoReq info)
			throws SbmSDKException;

	
}
