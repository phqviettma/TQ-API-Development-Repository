package com.tq.googlecalendar.lambda.handler;

import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.resp.GoogleShowCalendarResp;
import com.tq.googlecalendar.lambda.resp.ShowCalendarHandleResponse;
import com.tq.googlecalendar.req.TokenReq;
import com.tq.googlecalendar.resp.GoogleCalendarList;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class ShowGoogleCalendarHandler implements Handler {
	private TokenGoogleCalendarService googleTokenService = null;
	private Env env = null;
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = null;
	public ShowGoogleCalendarHandler(TokenGoogleCalendarService googleTokenService, Env env, GoogleCalendarApiServiceBuilder apiServiceBuilder) {
		this.env = env;
		this.googleTokenService = googleTokenService;
		this.apiServiceBuilder = apiServiceBuilder;
	}

	@Override
	public GoogleConnectStatusResponse handle(GoogleRegisterReq req)
			throws GoogleApiSDKException, TrueQuitRegisterException, SbmSDKException, InfSDKExecption {
		TokenReq tokenReq = new TokenReq(env.getGoogleClientId(), env.getGoogleClientSecrets(),
				req.getParams().getRefreshToken());
		TokenResp tokenResponse = googleTokenService.getToken(tokenReq);
		GoogleCalendarApiService googleApiService = apiServiceBuilder.build(tokenResponse.getAccess_token());
		GoogleCalendarList listCalendar = googleApiService.getListCalendar();
		GoogleConnectStatusResponse response = new GoogleConnectStatusResponse();
		response.setStatus("success");
		response.setSucceeded(true);
		GoogleShowCalendarResp googleShowCalendarResp = new GoogleShowCalendarResp(listCalendar,
				req.getParams().getEmail(), req.getParams().getRefreshToken(), req.getParams().getFirstName(),
				req.getParams().getLastName(), req.getParams().getGoogleEmail());
		ShowCalendarHandleResponse responseBody = new ShowCalendarHandleResponse(googleShowCalendarResp);
		response.setResponseBody(responseBody);

		return response;
	}

}
