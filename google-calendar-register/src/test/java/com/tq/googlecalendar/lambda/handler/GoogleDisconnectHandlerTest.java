package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
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
	private GoogleRegisterReq req = null;
	private TokenGoogleCalendarImpl tokenCalendarService = mock(TokenGoogleCalendarImpl.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private GoogleCalRenewService googleWatchChannelDbService = mock(GoogleCalRenewService.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingsService = mock(SbmSyncFutureBookingsService.class);
	private SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private SbmBookingInfoService sbmBookingInfoService = mock(SbmBookingInfoService.class);
	private GoogleDisconnectCalendarHandler disconnectHandler = new GoogleDisconnectCalendarHandler(mockedeEnv,
			calendarService, tokenCalendarService, mockedApiServiceBuilder, googleWatchChannelDbService,
			modifiedChannelService, sbmSyncFutureBookingsService, sbmListBookingService, sbmBookingInfoService);

	@Before
	public void init() throws GoogleApiSDKException {
		req = new GoogleRegisterReq();
		req.setAction("disconnect");
		GoogleRegisterParams params = new GoogleRegisterParams();
		params.setEmail("suongpham53@gmail.com");
		req.setParams(params);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("accesstoken");
		when(tokenCalendarService.getTokenIfValidResponse(any())).thenReturn(tokenResp);
	}

	@Test
	public void testDisconnect() throws TrueQuitRegisterException, GoogleApiSDKException {
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = Arrays.asList(new GoogleCalendarSbmSync("1-7",
				"phamthanhcute11@gmail.com", "phamthanhcute11@gmail.com", "suong", "pham", "", "-BLANK-", ""));

		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
		List<GoogleRenewChannelInfo> renewChannelInfo = Arrays.asList(new GoogleRenewChannelInfo());
		when(googleWatchChannelDbService.queryEmail(any())).thenReturn(renewChannelInfo);

		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockedApiServiceBuilder.build(anyString())).thenReturn(googleCalendarApiService);
		ErrorResp value = new ErrorResp();
		Error error = new Error();
		error.setCode(404);
		error.setMessage("Channel id not found for project id");
		value.setError(error);
		when(googleCalendarApiService.stopWatchEvent(any())).thenReturn(value);
		List<GCModifiedChannel> modifiedChannels = Arrays.asList(new GCModifiedChannel());
		when(modifiedChannelService.queryEmail(any())).thenReturn(modifiedChannels);
		SbmBookingList sbmBookingList = new SbmBookingList();
		sbmBookingList.setSbmId("sbmId");
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		
		// refresh token is valid
		when(tokenCalendarService.revokeToken(any())).thenReturn(true);
		GoogleConnectStatusResponse response = disconnectHandler.handle(req);
		assertEquals(response.isSucceeded(), true);
		
		// revoking token failed
		when(tokenCalendarService.revokeToken(any())).thenReturn(false);
		response = disconnectHandler.handle(req);
		assertEquals(response.isSucceeded(), true);
		
		// refresh token has been expired
		when(tokenCalendarService.getTokenIfValidResponse(any())).thenReturn(null);
		response = disconnectHandler.handle(req);
		assertEquals(response.isSucceeded(), true);
	}
}
