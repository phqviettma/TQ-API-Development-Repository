package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.GetUnitListReq;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.CompanyTimeZone;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.utils.SbmExecute;
import com.tq.simplybook.utils.SbmUtils;

public class GetUnitInfoTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private UnitServiceSbmImpl unitService = new UnitServiceSbmImpl();
	private SpecialdayServiceSbmImpl specialDayService = new SpecialdayServiceSbmImpl();

	 @Test
	public void testGetUnitList() throws Exception {

		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "1900561594";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		List<UnitProviderInfo> info = unitService.getUnitList(companyLogin, endpoint, userToken, true, true, 1);
		
		String json = SbmExecute.executeWithUserToken(companyLogin, endpoint, userToken, "getUnitList",
				new GetUnitListReq(true,true, 1));
		System.out.println(json);
		assertNotNull(info);

	}

	// @Test
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

	@Test
	public void testGetWorkDayInfo() throws SbmSDKException {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "1900561594";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		specialDayService.getWorkDaysInfo(companyLogin, endpoint, userToken, 2, 1, new FromDate("2018-02-21", "00:00"),
				new ToDate("2018-02-21", "00:00"));
	}

	@Test
	public void testGetUnitTime() throws Exception {
		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "1900561594";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		unitService.getUnitWorkDayInfo(companyLogin, endpoint, userToken, "2017-12-31", "2017-12-31", 6);

	}
}
