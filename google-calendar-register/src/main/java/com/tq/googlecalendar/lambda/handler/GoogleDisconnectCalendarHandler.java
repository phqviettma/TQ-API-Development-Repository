package com.tq.googlecalendar.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.context.Env;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.req.StopWatchEventReq;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;

public class GoogleDisconnectCalendarHandler implements Handler {
	private static final Logger m_log = LoggerFactory.getLogger(GoogleDisconnectCalendarHandler.class);

	private Env eVariables = null;
	private GoogleCalendarDbService googleCalendarService = null;
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	
	public GoogleDisconnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			TokenGoogleCalendarService tokenCalendarService) {
		this(eVariables, googleCalendarService, tokenCalendarService, new GoogleCalendarApiServiceBuilder());
	}
	
	public GoogleDisconnectCalendarHandler(Env eVariables, GoogleCalendarDbService googleCalendarService,
			TokenGoogleCalendarService tokenCalendarService, GoogleCalendarApiServiceBuilder apiServiceBuilder) {
		this.eVariables = eVariables;
		this.googleCalendarService = googleCalendarService;
		this.tokenCalendarService = tokenCalendarService;
		this.apiServiceBuilder = apiServiceBuilder;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, TrueQuitRegisterException {
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		String sbmEmail = req.getParams().getEmail();
		GoogleCalendarSbmSync googleCalendarSbmSync = googleCalendarService.query(sbmEmail);
		if (googleCalendarSbmSync != null) {
			TokenReq tokenReq = new TokenReq(eVariables.getGoogleClientId(), eVariables.getGoogleClientSecrets(),
					googleCalendarSbmSync.getRefreshToken());

			TokenResp tokenResp = tokenCalendarService.getToken(tokenReq);
			GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResp.getAccess_token());
			StopWatchEventReq stopEventReq = new StopWatchEventReq(googleCalendarSbmSync.getSbmId(),
					googleCalendarSbmSync.getGcWatchResourceId());
			// work-around: can' stop channel in just one request. So as to make sure the
			// channel is stopped successfully, we try to stop maximum of 3 times .
			for (int i = 0; i <= 3; i++) {
				ErrorResp errorResp = googleApiService.stopWatchEvent(stopEventReq);
				if (errorResp != null) {
					String errorMessage = errorResp.getError().getMessage();
					if (errorMessage.contains("Channel") && errorMessage.contains("not found")) {
						break;
					} else {
						throw new GoogleApiSDKException("Backend error");
					}
				}
			}

			googleCalendarService.delete(googleCalendarSbmSync);
			m_log.info("Delete successfully");

		} else {
			throw new TrueQuitRegisterException("The email " + sbmEmail + " is not connected yet ");
		}
		response.setSucceeded(true);
		return response;

	}

}
