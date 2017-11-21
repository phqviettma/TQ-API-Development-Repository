package com.tq.simplybook.impl;

import java.util.HashSet;
import java.util.Set;

import com.tq.simplybook.context.SbmParameterContext;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfoInfoReq;
import com.tq.simplybook.req.SetWorkDayInfoReq;
import com.tq.simplybook.req.WorkdayInfoReq;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.SpecialdayServiceSbm;

public class SbmBreakTimeManagement {
	private SpecialdayServiceSbm sss = new SpecialdayServiceSbmImpl();
	
	public boolean addBreakTime(SbmParameterContext parmsContext) throws SbmSDKException {
		WorkdayInfoReq info = new WorkdayInfoReq();
		info.setFrom("");
		info.setTo("");
		info.setEvent_id(parmsContext.getEventId());
		info.setUnit_id(parmsContext.getUnitId());
		WorksDayInfoResp workDayInfo = sss.getWorkDaysInfo(parmsContext.getCompanyLogin(), parmsContext.getEndpoint(), parmsContext.getUserToken(), info);
		SetWorkDayInfoInfoReq dayInfo = new SetWorkDayInfoInfoReq();
		dayInfo.setEvent_id(String.valueOf(parmsContext.getEventId()));
		dayInfo.setUnit_group_id(String.valueOf(parmsContext.getUnitId()));
		dayInfo.setStart_time(null);
		dayInfo.setEnd_time(null);
		Set<Breaktime> breakTimes = new HashSet<>();
		breakTimes.add(new Breaktime());
		dayInfo.setBreaktime(breakTimes);
		dayInfo.setDate("");
		SetWorkDayInfoReq setWorkDayInfo = new SetWorkDayInfoReq(dayInfo);
		sss.blockTimeSlot(parmsContext.getCompanyLogin(),parmsContext.getEndpoint(), parmsContext.getUserToken(), setWorkDayInfo);
		
		return false;
	}
	
	public boolean removeBreakTime(SbmParameterContext paraContext) throws SbmSDKException {
		WorkdayInfoReq info = new WorkdayInfoReq();
		info.setEvent_id(paraContext.getEventId());
		info.setUnit_id(paraContext.getUnitId());
		info.setFrom(paraContext.getFrom());
		info.setTo(paraContext.getTo());
		WorksDayInfoResp workDayInfo = sss.getWorkDaysInfo(paraContext.getCompanyLogin(),paraContext.getEndpoint(),paraContext.getUserToken(), info);
		SetWorkDayInfoInfoReq dayInfo = new SetWorkDayInfoInfoReq();
		dayInfo.setDate(workDayInfo.getDate());
		//TODO: Xem thu trong workday Info có cái break time đó ko , nếu có thì remove đi
		Set<Breaktime> breaktimes = new HashSet<>();
		breaktimes.add(new Breaktime(paraContext.getStartBreakTime(), paraContext.getEndBreakTime()));
		for( WorkTimeSlot timeInfo : workDayInfo.getInfo()) {
			
		}
		
		SetWorkDayInfoReq setWorkDayInfo = new SetWorkDayInfoReq(dayInfo);
		sss.unlockTimeSlot(paraContext.getCompanyLogin(), paraContext.getEndpoint(),paraContext.getUserToken(), setWorkDayInfo);
		return false;
	}
}
