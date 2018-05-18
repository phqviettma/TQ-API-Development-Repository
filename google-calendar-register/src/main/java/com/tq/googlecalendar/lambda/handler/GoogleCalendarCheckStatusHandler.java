package com.tq.googlecalendar.lambda.handler;

import java.util.List;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
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

		List<GoogleCalendarSbmSync> googleCalendarSbmSync = googleCalendarService
				.queryEmail(req.getParams().getEmail());
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		if (googleCalendarSbmSync.isEmpty()) {
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
