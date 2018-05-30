package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
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
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleConnectHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private static Env mockedeEnv = MockUtil.mockEnv();
	private static TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private TokenGoogleCalendarImpl tokenCalendarService = mock(TokenGoogleCalendarImpl.class);
	private SbmUnitService sbmUnitService = mock(SbmUnitService.class);
	private GoogleCalRenewService googleWatchChannelDbService = mock(GoogleCalRenewService.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = mock(SbmSyncFutureBookingsService.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private BookingServiceSbmImpl bookingSbmService = mock(BookingServiceSbmImpl.class);
	private SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private GoogleConnectCalendarHandler connectHandler = new GoogleConnectCalendarHandler(mockedeEnv, calendarService,
			contactItemService, tokenCalendarService, sbmUnitService, tokenService, mockedApiServiceBuilder,
			googleWatchChannelDbService, modifiedChannelService, sbmSyncFutureBookingService, bookingSbmService,
			sbmListBookingService);

	@Test
	public void testConnectHandler()
			throws TrueQuitRegisterException, GoogleApiSDKException, SbmSDKException, InfSDKExecption {
		GoogleRegisterReq req = new GoogleRegisterReq();
		req.setAction("connect");
		GoogleRegisterParams params = new GoogleRegisterParams();
		params.setEmail("testingdev@tma.com.vn");
		params.setRefreshToken("");
		params.setGoogleEmail("canh5870@gmail.com");
		params.setFirstName("Suong");
		params.setLastName("Pham");
		List<String> googleCalendarId = Arrays.asList("canh5870@gmail.com");
		params.setGoogleCalendarId(googleCalendarId);
		req.setParams(params);
		ContactItem contactItem = new ContactItem();
		contactItem.setEmail("testingdev@tma.com.vn");
		when(contactItemService.load(any())).thenReturn(contactItem);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(calendarService).put(any(GoogleCalendarSbmSync.class));
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("accesstoken");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		List<UnitProviderInfo> value = new ArrayList<>();
		UnitProviderInfo provider = new UnitProviderInfo();
		provider.setEmail("testingdev@tma.com.vn");
		Map<String, Object> event_map = new HashMap<>();
		event_map.put("2", null);
		provider.setEvent_map(event_map);
		value.add(provider);
		when(sbmUnitService.getUnitList(any(), any(), any(), any(), any(), any())).thenReturn(value);
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockedApiServiceBuilder.build(anyString())).thenReturn(googleCalendarApiService);
		WatchEventResp watchResponse = new WatchEventResp();
		watchResponse.setExpiration(1520671572000L);
		when(googleCalendarApiService.watchEvent(any(), any())).thenReturn(watchResponse);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(calendarService).put(any(GoogleCalendarSbmSync.class));
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(googleWatchChannelDbService).put(any(GoogleRenewChannelInfo.class));
		GoogleConnectStatusResponse response = connectHandler.handle(req);
		assertEquals(response.isSucceeded(), true);

	}
}
