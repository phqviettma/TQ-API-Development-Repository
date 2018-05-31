package com.tq.simplybook.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.resp.Breaktime;

public class SpecialDayServiceSbmImplTest {
	private static TokenServiceImpl tokenService = new TokenServiceImpl();
	private SpecialdayServiceSbmImpl specialDayService = new SpecialdayServiceSbmImpl();

	@Test
	public void testSetWorkDayInfo() throws Exception {

		String companyLogin = "canhcanh";
		String endpoint = "https://user-api.simplybook.asia/admin/";
		String endpoint_login = "https://user-api.simplybook.asia/login";
		String username = "admin";
		String password = "";
		String userToken = tokenService.getUserToken(companyLogin, username, password, endpoint_login);
		Set<Breaktime> breaktime = new HashSet<>();
		Breaktime breakTime = new Breaktime("00:00:00", "00:00:00");
		breaktime.add(breakTime);
		SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq("09:00:00", "19:00:00", 0, null, 0, "2018-05-14",
				"2018-05-14", "18", "2");

		SetWorkDayInfoReq setWorkDayInfo = new SetWorkDayInfoReq(info);
		specialDayService.changeWorkDay(companyLogin, endpoint, userToken, setWorkDayInfo);

	}
}
