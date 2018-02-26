package com.tq.simplybook.service;

import java.util.List;
import java.util.Map;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.resp.UnitWorkingTime;

public interface UnitServiceSbm {
	public List<UnitProviderInfo> getUnitList(String companyLogin, String endpoint, String token, Boolean isVisibleOnly,
			Boolean asArray, Integer handleClasses) throws SbmSDKException;

	public CompanyTimeZone getCompanyTimeZone(String companyLogin, String endpoint, String token)
			throws SbmSDKException;

	public Map<String,UnitWorkingTime> getUnitWorkDayInfo(String companyLogin,String endpoint, String token,String dateStart, String dateEnd, int unitGroupId) throws SbmSDKException;
}
