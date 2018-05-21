package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.lambda.exception.TrueQuitRegisterException;
import com.tq.googlecalendar.lambda.model.GoogleRegisterParams;
import com.tq.googlecalendar.lambda.model.GoogleRegisterReq;
import com.tq.googlecalendar.lambda.resp.GoogleConnectStatusResponse;
import com.tq.googlecalendar.resp.Error;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;

public class GoogleDisconnectHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private static Env mockedeEnv = MockUtil.mockEnv();
	private TokenGoogleCalendarImpl tokenCalendarService = mock(TokenGoogleCalendarImpl.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private GoogleCalRenewService googleWatchChannelDbService = mock(GoogleCalRenewService.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private GoogleDisconnectCalendarHandler disconnectHandler = new GoogleDisconnectCalendarHandler(mockedeEnv,
			calendarService, tokenCalendarService, mockedApiServiceBuilder, googleWatchChannelDbService,
			modifiedChannelService);

	@Test
	public void testDisconnect() throws TrueQuitRegisterException, GoogleApiSDKException {
		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("disconnect");
		GoogleRegisterParams params = new GoogleRegisterParams();
		params.setEmail("suongpham53@gmail.com");
		req.setParams(params);
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = Arrays.asList(new GoogleCalendarSbmSync("1-7",
				"phamthanhcute11@gmail.com", "phamthanhcute11@gmail.com", "suong", "pham", "", "-BLANK-", ""));

		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
		List<GoogleRenewChannelInfo> renewChannelInfo = Arrays.asList(new GoogleRenewChannelInfo());
		when(googleWatchChannelDbService.queryEmail(any())).thenReturn(renewChannelInfo);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("accesstoken");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockedApiServiceBuilder.build(anyString())).thenReturn(googleCalendarApiService);
		ErrorResp value = new ErrorResp();
		Error error = new Error();
		error.setCode(404);
		error.setMessage("Channel id not found for project id");
		value.setError(error);
		when(googleCalendarApiService.stopWatchEvent(any())).thenReturn(value);
		GoogleConnectStatusResponse response = disconnectHandler.handle(req);
		assertEquals(response.isSucceeded(), true);

	}
}
