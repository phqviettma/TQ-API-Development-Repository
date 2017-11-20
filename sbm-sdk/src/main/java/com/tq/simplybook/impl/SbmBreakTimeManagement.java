package com.tq.simplybook.impl;

import java.util.HashSet;
import java.util.Set;

import com.tq.simplybook.context.SbmParameterContext;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SetWorkDayInfo;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.DayInfo;
import com.tq.simplybook.resp.TimeInfo;
import com.tq.simplybook.resp.WorksDayInfo;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.test.WorkdayInfo;

public class SbmBreakTimeManagement {
	private SpecialdayServiceSbm sss = new SpecialdayServiceSbmImpl();
	
	public boolean addBreakTime(SbmParameterContext parmsContext) throws SbmSDKException {
		WorkdayInfo info = new WorkdayInfo();
		info.setFrom("");
		info.setTo("");
		info.setEvent_id(parmsContext.getEventId());
		info.setUnit_id(parmsContext.getUnitId());
		WorksDayInfo workDayInfo = sss.getWorkDaysInfo(parmsContext.getCompanyLogin(), parmsContext.getEndpoint(), parmsContext.getUserToken(), info);
		DayInfo dayInfo = new DayInfo();
		dayInfo.setEvent_id(String.valueOf(parmsContext.getEventId()));
		dayInfo.setUnit_group_id(String.valueOf(parmsContext.getUnitId()));
		dayInfo.setStart_time(null);
		dayInfo.setEnd_time(null);
		Set<Breaktime> breakTimes = new HashSet<>();
		breakTimes.add(new Breaktime());
		dayInfo.setBreaktime(breakTimes);
		dayInfo.setDate("");
		SetWorkDayInfo setWorkDayInfo = new SetWorkDayInfo(dayInfo);
		sss.blockTimeSlot(parmsContext.getCompanyLogin(),parmsContext.getEndpoint(), parmsContext.getUserToken(), setWorkDayInfo);
		
		return false;
	}
	
	public boolean removeBreakTime(SbmParameterContext paraContext) throws SbmSDKException {
		WorkdayInfo info = new WorkdayInfo();
		info.setEvent_id(paraContext.getEventId());
		info.setUnit_id(paraContext.getUnitId());
		info.setFrom(paraContext.getFrom());
		info.setTo(paraContext.getTo());
		WorksDayInfo workDayInfo = sss.getWorkDaysInfo(paraContext.getCompanyLogin(),paraContext.getEndpoint(),paraContext.getUserToken(), info);
		DayInfo dayInfo = new DayInfo();
		dayInfo.setDate(workDayInfo.getDate());
		//TODO: Xem thu trong workday Info có cái break time đó ko , nếu có thì remove đi
		Set<Breaktime> breaktimes = new HashSet<>();
		breaktimes.add(new Breaktime(paraContext.getStartBreakTime(), paraContext.getEndBreakTime()));
		for( TimeInfo timeInfo : workDayInfo.getInfo()) {
			
		}
		
		SetWorkDayInfo setWorkDayInfo = new SetWorkDayInfo(dayInfo);
		sss.unlockTimeSlot(paraContext.getCompanyLogin(), paraContext.getEndpoint(),paraContext.getUserToken(), setWorkDayInfo);
		return false;
	}
}
