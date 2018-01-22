package com.tq.calendarsbmsync.lambda.handler;

import java.util.List;

import com.tq.calendar.resp.Items;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public interface GoogleCalendarInternalHandler {
	void handle(List<Items> item, String sbmId) throws SbmSDKException, InfSDKExecption;
}
