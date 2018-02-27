package com.tq.simplybook.service;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.CompanyTimeZone;

public interface SbmSettingService {
	public CompanyTimeZone getCompanyTimeZone(String companyLogin, String endpoint, String token)
			throws SbmSDKException;
}
