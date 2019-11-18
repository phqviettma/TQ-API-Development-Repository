package com.tq.simplybook.service;

import java.util.Map;

import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.DeleteSpecialDayReq;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.req.WorkCalendarReq;
import com.tq.simplybook.resp.WorkCalendarResp;
import com.tq.simplybook.resp.WorksDayInfoResp;

public interface SpecialdayServiceSbm {
	boolean changeWorkDay(String companyLogin, String endpoint, String token, SetWorkDayInfoReq info)
			throws SbmSDKException;
	
	Map<String, WorksDayInfoResp> getWorkDaysInfo(String companyLogin, String endpoint, String token, int unit_id, int event_id, FromDate fromDate, ToDate toDate) throws SbmSDKException;
	
	WorkCalendarResp getWorkCalendar(String companyLogin, String endpoint, String token, WorkCalendarReq req);
	
	boolean deleteSpecialDay(String companyLogin, String endpoint, String token, DeleteSpecialDayReq req);
}
