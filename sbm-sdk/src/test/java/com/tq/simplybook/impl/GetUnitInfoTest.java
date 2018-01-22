package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.resp.UnitProviderInfo;

public class GetUnitInfoTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private UnitServiceSbmImpl unitService = new UnitServiceSbmImpl();
	private SpecialdayServiceSbmImpl specialDayService = new SpecialdayServiceSbmImpl();
	@Test
	public void testGetUnitList() throws SbmSDKException {

		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		List<UnitProviderInfo> info = unitService.getUnitList(companyLogin, endpoint, userToken, true, true, 1);
		System.out.println(info);
		assertNotNull(info);

	}

	//@Test
	public void testGetCompanyTimeZone() throws SbmSDKException {
		String companyLogin = "trancanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		CompanyTimeZone timeZone = unitService.getCompanyTimeZone(companyLogin, endpoint, userToken);
		System.out.println(timeZone.getTimezone());
	}

	//@Test
	public void testGetWorkDayInfo() throws SbmSDKException {
		String companyLogin = "trancanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
			 specialDayService.getWorkDaysInfo(companyLogin, endpoint, userToken, 1, 2, new FromDate("2018-01-01", "09:00"), new ToDate("2018-01-01","12:00"));
	}
}
