package com.tq.calendarsbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarModifiedSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.CalendarEvents;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.Items;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class GoogleCalendarHandlerTest {
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private Env env = MockUtil.mockEnv();
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private SpecialdayServiceSbm specialdayService = mock(SpecialdayServiceSbm.class);
	private SbmBreakTimeManagement sbmBreakTimeManagement = new SbmBreakTimeManagement();
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private SbmUnitService unitService = mock(SbmUnitService.class);
	private GoogleCalendarApiServiceBuilder mockedApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private CreateGoogleCalendarEventHandler handler = new CreateGoogleCalendarEventHandler(env, tokenService,
			specialdayService, sbmBreakTimeManagement, sbmGoogleCalendarService, unitService);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private ContactServiceInf contactInfService = mock(ContactServiceInf.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private SbmGoogleCalendarDbService sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private BookingServiceSbm bookingService = mock(BookingServiceSbm.class);
	private DeleteGoogleCalendarEventHandler deleteHandler = new DeleteGoogleCalendarEventHandler(env, tokenService,
			googleCalendarService, specialdayService, sbmBreakTimeManagement, contactItemService, contactInfService,
			sbmCalendarService, bookingService, unitService);
	GoogleCalendarHandler calendarHanler = new GoogleCalendarHandler(env, amazonDynamoDB, googleCalendarService,
			specialdayService, handler, deleteHandler, unitService, modifiedChannelService, tokenCalendarService, mockedApiServiceBuilder);
	private Context context = mock(Context.class);

	@Test
	public void test() throws GoogleApiSDKException{
		AwsProxyRequest req = new AwsProxyRequest();
		String body = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("sync_message.json"));
		req.setBody(body);
		GoogleCalendarSbmSync googleCalendarSbm = new GoogleCalendarSbmSync();
		googleCalendarSbm.setSbmId("2-2");
		googleCalendarSbm.setEmail("tmatesting@gmail.com");
		googleCalendarSbm.setGoogleEmail("canh5870@gmail.com");
		googleCalendarSbm.setRefreshToken("");
		ClientInfo ci = new ClientInfo();
		ci.setContactId(496);
		ContactItem contactItem = new ContactItem();
		contactItem.setClient(ci);
		when(contactItemService.load(any())).thenReturn(contactItem);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		SbmGoogleCalendar sbmGoogleCalendar = new SbmGoogleCalendar();
		sbmGoogleCalendar.setEventId("4htksb3u20j48p4jj3o1mhnjqo");
		sbmGoogleCalendar.setAgent("google");
		sbmGoogleCalendar.setFlag(1);
		sbmGoogleCalendar.setSbmId(24L);
		GCModifiedChannel modifiedItem = new GCModifiedChannel();
		modifiedItem.setCheckingStatus(0);
		when(modifiedChannelService.load(any())).thenReturn(modifiedItem);
		when(sbmCalendarService.queryWithIndex(any())).thenReturn(sbmGoogleCalendar);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("Access_Token");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp );
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockedApiServiceBuilder.build(any())).thenReturn(googleCalendarApiService);
		GoogleCalendarSettingsInfo settingInfo = new GoogleCalendarSettingsInfo();
		settingInfo.setValue("Asia/Saigon");
		when(googleCalendarApiService.getSettingInfo(any())).thenReturn(settingInfo );
		CalendarEvents calendarEvents = new CalendarEvents();
		List<Items> items = Arrays.asList(new Items());
		calendarEvents.setItems(items );
		when(googleCalendarApiService.queryNewestEvent(any(), any(), any())).thenReturn(calendarEvents );
		AwsProxyResponse response = calendarHanler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}

}
