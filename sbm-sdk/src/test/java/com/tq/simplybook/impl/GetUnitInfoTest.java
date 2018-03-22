package com.tq.simplybook.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkingTime;

public class GetUnitInfoTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private SbmUnitServiceImpl unitService = new SbmUnitServiceImpl();
	private SpecialdayServiceSbmImpl specialDayService = new SpecialdayServiceSbmImpl();

	@Test
	public void testGetUnitList() throws Exception {

		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		List<UnitProviderInfo> info = unitService.getUnitList(companyLogin, endpoint, userToken, true, true, 1);
		assertNotNull(info);

	}

	@Test
	public void testGetWorkDayInfo() throws SbmSDKException {
		String companyLogin = "canhchi";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		specialDayService.getWorkDaysInfo(companyLogin, endpoint, userToken, 4, 2, new FromDate("2018-03-22", "00:00"),
				new ToDate("2018-03-22", "00:00"));
	}

	@Test
	public void testGetUnitTime() throws Exception {
		String companyLogin = "canhchi";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		Map<String, UnitWorkingTime> unitWorkingDayInfoMap = unitService.getUnitWorkDayInfo(companyLogin, endpoint,
				userToken, "2018-03-20", "2018-03-23", 4);
		UnitWorkingTime unitWorkingTime = unitWorkingDayInfoMap.get("2018-03-20");
		Map<String, WorkingTime> unitWorkingTimeMap = unitWorkingTime.getWorkingTime();
		WorkingTime workingTime = unitWorkingTimeMap.get(String.valueOf(4));
		assertNotNull(workingTime.getStart_time());
	}

}
