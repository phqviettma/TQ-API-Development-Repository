package com.tq.simplybook.impl;

import java.util.ArrayList;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.resp.TimeZoneDetail;
import com.tq.simplybook.service.SbmSettingService;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class SbmSettingServiceImpl implements SbmSettingService{

	@Override
	public CompanyTimeZone getCompanyTimeZone(String companyLogin, String endpoint, String token)
			throws SbmSDKException {
		try {
			String jsonResp = SbmExecute.executeWithUserToken(companyLogin, endpoint, token, "getCompanyTimezoneOffset",
					new ArrayList<String>());

			TimeZoneDetail readValueForObject = SbmUtils.readValueForObject(jsonResp, TimeZoneDetail.class);
			return readValueForObject.getResult();
		} catch (Exception e) {
			throw new SbmSDKException(e.getMessage() + " during getCompanyTimezoneOffset()", e);
		}

	}

}
