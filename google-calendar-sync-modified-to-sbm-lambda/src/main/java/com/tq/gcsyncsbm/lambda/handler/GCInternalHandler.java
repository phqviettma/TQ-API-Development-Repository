package com.tq.gcsyncsbm.lambda.handler;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.googlecalendar.resp.Items;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public interface GCInternalHandler {
	void handle(List<Items> item, String sbmId, GoogleCalendarSbmSync googleCalendarSbmSync) throws SbmSDKException, InfSDKExecption;
}
