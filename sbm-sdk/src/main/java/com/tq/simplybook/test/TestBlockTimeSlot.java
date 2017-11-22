package com.tq.simplybook.test;

import java.util.HashSet;
import java.util.Set;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class TestBlockTimeSlot {
	public static final String COMPANY_LOGIN = "vuongtran";
	public static final String END_POINT = "https://user-api.simplybook.me/admin/";
	public static final String USERNAME = "admin";
	public static final String PASSWORD = "y#EDyMA3E3";
	public static final String ENDPOINT_SBM_LOGIN = "https://user-api.simplybook.me/login";

	public static void main(String[] args) throws SbmSDKException {
		SpecialdayServiceSbm timeSlotService = new SpecialdayServiceSbmImpl();
		TokenServiceSbm tokenService = new TokenServiceImpl();
		String token = tokenService.getUserToken(COMPANY_LOGIN, USERNAME, PASSWORD, ENDPOINT_SBM_LOGIN);
		
		  Set<Breaktime> breakTime = new HashSet<Breaktime>();
		  breakTime.add(new Breaktime("9:40", "9:45"));
		 SetWorkDayInfoInfoReq info = new SetWorkDayInfoInfoReq("08:20", "18:00", 0, breakTime, 0, "",
		  "2017-11-17", "1", "1");
		  //Boolean s = timeSlotService.unlockTimeSlot(COMPANY_LOGIN, END_POINT, token, newSetWorkDayInfoReq(info));
		 /* Boolean s = timeSlotService.blockTimeSlot(COMPANY_LOGIN, END_POINT, token, new
		 SetWorkDayInfo(info)); System.out.println(s);
		 */
		/*timeSlotService.getWorkDaysInfo(COMPANY_LOGIN, END_POINT, token, new
		WorkdayInfo("2017-11-17 9:00:00", "2017-11-17 10:00:00", 1,1));
*/
	}

}
