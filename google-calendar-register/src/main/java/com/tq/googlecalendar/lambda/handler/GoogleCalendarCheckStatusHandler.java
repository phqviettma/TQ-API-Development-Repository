package com.tq.googlecalendar.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class GoogleCalendarCheckStatusHandler implements Handler {
	private GoogleCalendarDbService googleCalendarService = null;
	public GoogleCalendarCheckStatusHandler(GoogleCalendarDbService googleCalendarService) {
		this.googleCalendarService = googleCalendarService;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, TrueQuitRegisterException, SbmSDKException, InfSDKExecption {

		GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.query(req.getParams().getEmail());
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		if (googleCalendarSbmSync == null) {
			response.setStatus("disconnected");
			response.setSucceeded(true);
			return response;
		} else {
			response.setStatus("connected");
			response.setSucceeded(true);
			return response;
		}
		
	}
}
