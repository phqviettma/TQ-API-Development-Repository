package com.tq.simplybook.service;

import java.util.List;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.resp.UnitProviderInfo;

public interface UnitServiceSbm {
	public List<UnitProviderInfo> getUnitList(String companyLogin, String endpoint, String token, Boolean isVisibleOnly,
			Boolean asArray, Integer handleClasses) throws SbmSDKException;

	public CompanyTimeZone getCompanyTimeZone(String companyLogin, String endpoint, String token) throws SbmSDKException;
}
