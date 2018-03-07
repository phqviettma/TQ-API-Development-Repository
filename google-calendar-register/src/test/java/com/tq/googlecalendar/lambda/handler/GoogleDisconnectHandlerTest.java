package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.context.Env;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.lambda.model.GoogleConnectStatusResponse;
import com.tq.googlecalendar.lambda.model.GoogleRegisterParams;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.resp.Error;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;

public class GoogleDisconnectHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private static Env mockedeEnv = MockUtil.mockEnv();
	private TokenGoogleCalendarImpl tokenCalendarService = mock(TokenGoogleCalendarImpl.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder =mock(GoogleCalendarApiServiceBuilder.class);
	private GoogleDisconnectCalendarHandler disconnectHandler = new GoogleDisconnectCalendarHandler(mockedeEnv,
			calendarService, tokenCalendarService, mockedApiServiceBuilder);
	@Test
	public void testDisconnect() throws TrueQuitRegisterException, GoogleApiSDKException{
		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("disconnect");
		GoogleRegisterParams params = new GoogleRegisterParams();
		params.setEmail("suongpham53@gmail.com");
		req.setParams(params);
		GoogleCalendarSbmSync googleCalendarSbmSync = new GoogleCalendarSbmSync("2-4", "phamthanhcute11@gmail.com",
				"testingdev@tma.com.vn", "suong", "pham", "", "1/mLAy7U8YxEuJFYdWd3eheQAfx6L13lrxgLlKiK40fOY",
				"-BLANK-", "9C0dOEpGs7L-ZBJy2BIC6AAQ8ak");

		when(calendarService.query(any())).thenReturn(googleCalendarSbmSync);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("accesstoken");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp );
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockedApiServiceBuilder.build(anyString())).thenReturn(googleCalendarApiService);
		ErrorResp value =new ErrorResp();
		Error error = new Error();
		error.setCode(404);
		error.setMessage("Channel id not found for project id");
		value.setError(error );
		when(googleCalendarApiService.stopWatchEvent(any())).thenReturn(value );
		GoogleConnectStatusResponse response = disconnectHandler.handle(req);
		assertEquals(response.isSucceeded(),true);
	
	}
}
