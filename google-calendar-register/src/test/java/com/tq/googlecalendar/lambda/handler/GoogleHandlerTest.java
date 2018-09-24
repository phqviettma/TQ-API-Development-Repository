package com.tq.googlecalendar.lambda.handler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.Error;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private static Env mockedeEnv = MockUtil.mockEnv();
	private static TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private static SbmUnitService unitService = mock(SbmUnitService.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private SbmUnitService sbmUnitService = mock(SbmUnitService.class);
	private TokenServiceSbm tokenServiceSbm = mock(TokenServiceSbm.class);
	private BookingServiceSbmImpl bookingSbmService = mock(BookingServiceSbmImpl.class);
	private SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private GoogleCalRenewService googleWatchChannelDbService = mock(GoogleCalRenewService.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = mock(SbmSyncFutureBookingsService.class);
	private GoogleCalendarCheckStatusHandler checkHandler = new GoogleCalendarCheckStatusHandler(calendarService);
	private SbmBookingInfoService sbmBookingInfoService = mock(SbmBookingInfoService.class);
	private GoogleHandler handler = null;
	private GoogleCalendarApiService googleApiService =null;
	private GoogleConnectCalendarHandler connectHandler = new GoogleConnectCalendarHandler(mockedeEnv, calendarService,
			contactItemService, tokenCalendarService, sbmUnitService, tokenServiceSbm, mockedApiServiceBuilder,
			googleWatchChannelDbService, modifiedChannelService, sbmSyncFutureBookingService, bookingSbmService,
			sbmListBookingService);

	private GoogleDisconnectCalendarHandler disconnectHandler = new GoogleDisconnectCalendarHandler(mockedeEnv,
			calendarService, tokenCalendarService, mockedApiServiceBuilder, googleWatchChannelDbService,
			modifiedChannelService, sbmSyncFutureBookingService, sbmListBookingService, sbmBookingInfoService);
	private ShowGoogleCalendarHandler showCalendarHandleResponse = new ShowGoogleCalendarHandler(tokenCalendarService, mockedeEnv, mockedApiServiceBuilder);

	@Before
	public void init() throws GoogleApiSDKException, SbmSDKException {

		Env.mock(mockedeEnv);

		handler = new GoogleHandler(mockedeEnv, amazonDynamoDB, unitService, tokenService, calendarService,
				contactItemService, connectHandler, checkHandler, disconnectHandler, showCalendarHandleResponse);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		when(tokenServiceSbm.getUserToken(any(), any(), any(), any())).thenReturn("token");
		googleApiService = mock(GoogleCalendarApiService.class);
		when(mockedApiServiceBuilder.build(any())).thenReturn(googleApiService );
	}

	@Test
	public void testCheckHandler() {
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = Arrays.asList(new GoogleCalendarSbmSync());
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("check_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		ContactItem contactItem = new ContactItem();
		contactItem.setEmail("testingdev@tma.com.vn");
		when(contactItemService.load(any())).thenReturn(contactItem);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testHanleExceptionConnectHandler() {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("connect_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		when(contactItemService.load(any())).thenReturn(null);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		List<GoogleCalendarSbmSync> googleCalendarSbm = Arrays.asList();
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbm);
		assertEquals(404, response.getStatusCode());
		assertThat("The email phamthanhcute11@gmail.com is not signed up yet ",
				is("The email phamthanhcute11@gmail.com is not signed up yet "));
	}

	@Test
	public void testHandleException() throws SbmSDKException {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("connect_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		ContactItem contactItem = new ContactItem();
		contactItem.setEmail("email");
		when(contactItemService.load(any())).thenReturn(contactItem);
		List<GoogleCalendarSbmSync> googleCalendarSbm = Arrays.asList();
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbm);
		List<UnitProviderInfo> unitProvider = new ArrayList<>();
		when(sbmUnitService.getUnitList(any(), any(), any(), any(), any(), any())).thenReturn(unitProvider );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(404, response.getStatusCode());
		assertThat(
				"There is no Simplybook.me service provider associated to the provided e-mail phamthanhcute11@gmail.com",
				is("There is no Simplybook.me service provider associated to the provided e-mail phamthanhcute11@gmail.com"));
	}

	@Test
	public void handleExceptionAlConnected() {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("connect_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		List<GoogleCalendarSbmSync> googleCalendarSbm = new ArrayList<>();
		GoogleCalendarSbmSync calendarSbm = new GoogleCalendarSbmSync();
		calendarSbm.setChannelId("channel id");
		googleCalendarSbm.add(calendarSbm);
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbm);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertThat("The email phamthanhcute11@gmail.com is already connected",
				is("The email phamthanhcute11@gmail.com is already connected"));
		assertEquals(404, response.getStatusCode());
	}
	@Test
	public void testHandleConnect() throws GoogleApiSDKException, SbmSDKException {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("connect_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		List<GoogleCalendarSbmSync> googleCalendarSbm = Arrays.asList();
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbm );
		ContactItem contactItem = new ContactItem().withEmail("Email");
		when(contactItemService.load(any())).thenReturn(contactItem );
		List<UnitProviderInfo> unitProvider = new ArrayList<>();
		UnitProviderInfo provider = new UnitProviderInfo();
		unitProvider.add(provider);
		provider.setId("1");
		provider.setEmail("phamthanhcute11@gmail.com");
		Map<String, Object> event_map = new HashMap<>();
		event_map.put("2",null);
		provider.setEvent_map(event_map );
		when(sbmUnitService.getUnitList(any(), any(), any(), any(), any(), any())).thenReturn(unitProvider);
		WatchEventResp watchResp = new WatchEventResp();
		watchResp.setExpiration(1532649600000L);
		watchResp.setResourceId("resource id");
		watchResp.setId("id");
		when(googleApiService.watchEvent(any(), any())).thenReturn(watchResp );
		List<GetBookingResp> bookingResps = new ArrayList<>();
		bookingResps.add(new GetBookingResp());
		when(bookingSbmService.getBookings(any(),any(), any(), any())).thenReturn(bookingResps );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200,response.getStatusCode());
	}
	@Test
	public void testShowCalendarHandler() {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("show_calendar_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
	@Test
	public void testDisconnectHandler() throws GoogleApiSDKException {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("disconnect_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		List<GoogleCalendarSbmSync> googleCalendarSbms = new ArrayList<>();
		GoogleCalendarSbmSync calendarSbm = new GoogleCalendarSbmSync();
		calendarSbm.setRefreshToken("refresh_token");
		calendarSbm.setChannelId("channel id");
		calendarSbm.setGcWatchResourceId("resource id");
		googleCalendarSbms.add(calendarSbm );
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbms );
		ErrorResp errorResp = new ErrorResp();
		Error error = new Error();
		errorResp.setError(error );
		error.setMessage("Channel not found");
		when(googleApiService.stopWatchEvent(any())).thenReturn(errorResp );
		List<GCModifiedChannel> modifiedChannels = new ArrayList<>();
		GCModifiedChannel channel = new GCModifiedChannel();
		channel.setChannelId("channel id");
		modifiedChannels.add(channel );
		when(modifiedChannelService.queryEmail(any())).thenReturn(modifiedChannels );
		List<GoogleRenewChannelInfo> renewChannels = new ArrayList<>();
		GoogleRenewChannelInfo newChannelInfo = new GoogleRenewChannelInfo();
		newChannelInfo.setChannelId("new channel id");
		renewChannels.add(newChannelInfo );
		when(googleWatchChannelDbService.queryEmail(any())).thenReturn(renewChannels );
		SbmSyncFutureBookings sbmSyncFutureBookings = new SbmSyncFutureBookings();
		sbmSyncFutureBookings.setEmail("email");
		when(sbmSyncFutureBookingService.load(any())).thenReturn(sbmSyncFutureBookings );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
	@Test
	public void handleException() {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("error_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(400, response.getStatusCode());
	}
}
