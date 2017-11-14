package com.tq.simplybook.test;

import java.util.ArrayList;
import java.util.List;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.DayInfo;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class TestBlockTimeSlot {
	public static final String COMPANY_LOGIN = "lccanh";
	public static final String END_POINT = "https://user-api.simplybook.me/admin/";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "";
	public static final String ENDPOINT_SBM_LOGIN = "https://user-api.simplybook.me/login";

	public static void main(String[] args) throws SbmSDKException {
		SpecialdayServiceSbm timeSlotService = new SpecialdayServiceSbmImpl();
		TokenServiceSbm tokenService = new TokenServiceImpl();
		List<Breaktime> breakTime = new ArrayList<Breaktime>();
		/*breakTime.add(new Breaktime("08:30", "09:00"));
		breakTime.add(new Breaktime("13:20", "13:30"));*/
		String token = tokenService.getUserToken(COMPANY_LOGIN, USERNAME, PASSWORD, ENDPOINT_SBM_LOGIN);
		DayInfo info = new DayInfo("08:20", "18:00", 0, breakTime, 0, "", "2017-09-8", "1", "");
		//Boolean s = timeSlotService.unlockTimeSlot(COMPANY_LOGIN, END_POINT, token, new DayInfoReq(info));
		 Boolean s = timeSlotService.blockTimeSlot(COMPANY_LOGIN, END_POINT, token, new SetWorkDayInfo(info));
		System.out.println(s);
		
		
	}

}
