package com.tq.calendarsbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
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
	private SpecialdayServiceSbm specialdayService = new SpecialdayServiceSbmImpl();
	private SbmBreakTimeManagement sbmBreakTimeManagement = new SbmBreakTimeManagement();
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private SbmUnitService unitService = new SbmUnitServiceImpl();
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private CreateGoogleCalendarEventHandler handler = new CreateGoogleCalendarEventHandler(env, tokenService,
			specialdayService, sbmBreakTimeManagement, sbmGoogleCalendarService, unitService);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private ContactServiceInf contactInfService = new ContactServiceImpl();
	private SbmGoogleCalendarDbService sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private BookingServiceSbm bookingService = new BookingServiceSbmImpl();
	private DeleteGoogleCalendarEventHandler deleteHandler = new DeleteGoogleCalendarEventHandler(env, tokenService,
			googleCalendarService, specialdayService, sbmBreakTimeManagement, contactItemService, contactInfService,
			sbmCalendarService, bookingService, unitService);
	GoogleCalendarHandler calendarHanler = new GoogleCalendarHandler(env, amazonDynamoDB, googleCalendarService,
			specialdayService, handler, deleteHandler, unitService, modifiedChannelService);
	private Context context = mock(Context.class);

	@Test
	public void test() {
		AwsProxyRequest req = new AwsProxyRequest();
		String body = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("sync_message.json"));
		req.setBody(body);
		GoogleCalendarSbmSync googleCalendarSbm = new GoogleCalendarSbmSync();
		googleCalendarSbm.setSbmId("2-2");
		googleCalendarSbm.setEmail("tmatesting@gmail.com");
		googleCalendarSbm.setGoogleEmail("canh5870@gmail.com");
		googleCalendarSbm.setRefreshToken("");
		googleCalendarSbm.setLastQueryTimeMin("2018-05-09T08:37:35.000+07:00");
		googleCalendarSbm.setNextSyncToken("-BLANK-");
		googleCalendarSbm.setNextPageToken("EjYKKzVxZWlqZWgxOWNnMGJsYXVpN3Ezb2NsOGNzXzIwMTkwNDE5VDA0MDAwMFoYgNjrqIDc4QI=");
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
		modifiedItem.setCheckStatus(0);
		when(modifiedChannelService.load(any())).thenReturn(modifiedItem);
		when(sbmCalendarService.queryWithIndex(any())).thenReturn(sbmGoogleCalendar);
		AwsProxyResponse response = calendarHanler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}

}
