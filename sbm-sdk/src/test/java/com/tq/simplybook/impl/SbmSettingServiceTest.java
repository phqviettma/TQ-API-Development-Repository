package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.service.SbmSettingService;

public class SbmSettingServiceTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private SbmSettingService sbmSettingService = new SbmSettingServiceImpl();

	@Test
	public void testGetCompanyTimeZone() throws SbmSDKException {
		String companyLogin = "canhchi";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "1900561594";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		CompanyTimeZone timeZone = sbmSettingService.getCompanyTimeZone(companyLogin, endpoint, userToken);
		assertNotNull(timeZone.getTimezone());
	}
}
