package com.tq.simplybook.impl;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.GetWorkDayInfo;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.DayInfo;
import com.tq.simplybook.resp.WorkTimeDetail;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class SpecialdayServiceSbmImpl implements SpecialdayServiceSbm {

	@Override
	public boolean blockTimeSlot(String companyLogin, String endpoint, String userToken, SetWorkDayInfo info)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					info);
			WorkTimeDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, WorkTimeDetail.class);

			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during setWorkDayInfo()", e);
		}

	}

	@Override
	public boolean unlockTimeSlot(String companyLogin, String endpoint, String userToken, SetWorkDayInfo info)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "setWorkDayInfo",
					info);
			WorkTimeDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, WorkTimeDetail.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during setWorkDayInfo()", e);
		}

	}

	@Override
	public DayInfo getWorkDaysInfo(String companyLogin, String endpoint, String userToken) {
		String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "getWorkDayInfo", new GetWorkDayInfo());
		WorkTimeDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, WorkTimeDetail.class);
		
		return null;
	}

}
