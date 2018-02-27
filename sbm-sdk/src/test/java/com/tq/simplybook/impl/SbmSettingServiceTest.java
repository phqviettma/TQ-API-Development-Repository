package com.tq.simplybook.impl;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.service.SbmSettingService;

public class SbmSettingServiceTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private SbmSettingService sbmSettingService = new SbmSettingServiceImpl();

	@Test
	public void testGetCompanyTimeZone() throws SbmSDKException {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		CompanyTimeZone timeZone = sbmSettingService.getCompanyTimeZone(companyLogin, endpoint, userToken);
		System.out.println(timeZone.getTimezone());
	}
}
