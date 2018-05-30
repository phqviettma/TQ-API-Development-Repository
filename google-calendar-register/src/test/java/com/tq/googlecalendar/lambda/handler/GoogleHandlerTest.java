package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private static Env mockedeEnv = MockUtil.mockEnv();
	private static TokenServiceSbm tokenService =mock(TokenServiceSbm.class);
	private static SbmUnitService unitService = mock(SbmUnitService.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private SbmUnitService sbmUnitService = mock(SbmUnitService.class);
	private TokenServiceSbm tokenServiceSbm = mock(TokenServiceSbm.class);
	private BookingServiceSbmImpl bookingSbmService  = mock(BookingServiceSbmImpl.class);
	private  SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private GoogleCalRenewService googleWatchChannelDbService = mock(GoogleCalRenewService.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = mock(SbmSyncFutureBookingsService.class);
	private GoogleCalendarCheckStatusHandler checkHandler = new GoogleCalendarCheckStatusHandler(calendarService);
	private GoogleConnectCalendarHandler connectHandler = new GoogleConnectCalendarHandler(mockedeEnv, calendarService,
			contactItemService, tokenCalendarService, sbmUnitService, tokenServiceSbm,
			mockedApiServiceBuilder, googleWatchChannelDbService, modifiedChannelService, sbmSyncFutureBookingService, bookingSbmService, sbmListBookingService);


	private GoogleDisconnectCalendarHandler disconnectHandler = new GoogleDisconnectCalendarHandler(mockedeEnv,
			calendarService, tokenCalendarService, mockedApiServiceBuilder, googleWatchChannelDbService, modifiedChannelService, sbmSyncFutureBookingService, sbmListBookingService);

	@Test
	public void testRegisterHandler() {
		Env.mock(mockedeEnv);

		GoogleHandler handler = new GoogleHandler(mockedeEnv, amazonDynamoDB, unitService, tokenService,
				calendarService, contactItemService, connectHandler, checkHandler, disconnectHandler);

		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("user_info.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = Arrays.asList(new GoogleCalendarSbmSync());
		when(calendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);

		ContactItem contactItem = new ContactItem();
		contactItem.setEmail("testingdev@tma.com.vn");
		when(contactItemService.load(any())).thenReturn(contactItem);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(calendarService).put(any(GoogleCalendarSbmSync.class));
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
}
