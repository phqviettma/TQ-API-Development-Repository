package com.tq.simplybook.test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class TestBlockTimeSlot {
	public static final String COMPANY_LOGIN = "vuongtran";
	public static final String END_POINT = "https://user-api.simplybook.me/admin/";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "y#EDyMA3E3";
	public static final String ENDPOINT_SBM_LOGIN = "https://user-api.simplybook.me/login";
	private static SbmBreakTimeManagement sss = new SbmBreakTimeManagement();

	public static void main(String[] args) throws SbmSDKException {
		SpecialdayServiceSbm timeSlotService = new SpecialdayServiceSbmImpl();
		TokenServiceSbm tokenService = new TokenServiceImpl();
		String token = tokenService.getUserToken(COMPANY_LOGIN, USERNAME, PASSWORD, ENDPOINT_SBM_LOGIN);
		Set<Breaktime> breakTime = new HashSet<>();
		breakTime.add(new Breaktime("12:00:00", "12:30:00"));

		Map<String, WorksDayInfoResp> workDayInfoMap = timeSlotService.getWorkDaysInfo(COMPANY_LOGIN, END_POINT, token,
				1, 1, new FromDate("2017-11-24", "09:00:00"), new ToDate("2017-11-24", "18:00:00"));
		boolean a = sss.addBreakTime(COMPANY_LOGIN, END_POINT, token, 1, 1, "09:00:00", "18:00:00", "2017-11-24",
				breakTime, workDayInfoMap);
		System.out.println(a);
	}

	//@Test
	public void testGetWorkDayInfo() throws SbmSDKException {
		SpecialdayServiceSbm s = new SpecialdayServiceSbmImpl();
		TokenServiceSbm tokenService = new TokenServiceImpl();
		String token = tokenService.getUserToken(COMPANY_LOGIN, USERNAME, PASSWORD, ENDPOINT_SBM_LOGIN);
		Map<String, WorksDayInfoResp> a = s.getWorkDaysInfo(COMPANY_LOGIN, END_POINT, token, 1, 1,
				new FromDate("2017-11-27", "09:00:00"), new ToDate("2017-11-27", "18:00:00"));
		System.out.println(a);
	}

	//@Test
	public void testChangeWorkDay() throws SbmSDKException {
		SpecialdayServiceSbm s = new SpecialdayServiceSbmImpl();
		TokenServiceSbm tokenService = new TokenServiceImpl();
		String token = tokenService.getUserToken(COMPANY_LOGIN, USERNAME, PASSWORD, ENDPOINT_SBM_LOGIN);
		Set<Breaktime> breakTime = new HashSet<>();
		breakTime.add(new Breaktime("14:00:00", "14:30:00"));
		SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq("09:00:00", "18:00:00", 0, breakTime, 0, "2017-11-30",
				"2017-11-30", "1", "1");
		SetWorkDayInfoReq setWorkDayInfoReq = new SetWorkDayInfoReq(info);
		boolean a = s.changeWorkDay(COMPANY_LOGIN, END_POINT, token, setWorkDayInfoReq);
		System.out.println(a);
	}

}
