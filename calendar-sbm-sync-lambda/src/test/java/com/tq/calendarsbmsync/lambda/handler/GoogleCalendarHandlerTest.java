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
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.service.SbmUnitService;

public class GoogleCalendarHandlerTest {
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private Env env = MockUtil.mockEnv();
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private SpecialdayServiceSbm specialdayService = new SpecialdayServiceSbmImpl();
	private SbmBreakTimeManagement sbmBreakTimeManagement = new SbmBreakTimeManagement();
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private SbmUnitService unitService = new SbmUnitServiceImpl();
	private CreateGoogleCalendarEventHandler handler = new CreateGoogleCalendarEventHandler(env, tokenService,
			specialdayService, sbmBreakTimeManagement, sbmGoogleCalendarService, unitService);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private ContactServiceInf contactInfService = new ContactServiceImpl();
	private SbmGoogleCalendarDbService sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private BookingServiceSbm bookingService = new BookingServiceSbmImpl();
	private DeleteGoogleCalendarEventHandler deleteHandler = new DeleteGoogleCalendarEventHandler(env, tokenService,
			googleCalendarService, specialdayService, sbmBreakTimeManagement, contactItemService, contactInfService,
			sbmCalendarService, bookingService, unitService);
	GoogleCalendarHandler calendarHanler = new GoogleCalendarHandler(env, amazonDynamoDB, googleCalendarService, specialdayService,
			handler, deleteHandler, unitService);
	private Context context = mock(Context.class);

	@Test
	public void test() {
		AwsProxyRequest req = new AwsProxyRequest();
		String body = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("sync_message.json"));
		req.setBody(body);
		GoogleCalendarSbmSync googleCalendarSbm = new GoogleCalendarSbmSync();
		googleCalendarSbm.setSbmId("1-6");
		googleCalendarSbm.setEmail("trancanh27@gmail.com");
		googleCalendarSbm.setGoogleEmail("phamthanhcute11@gmail.com");
		googleCalendarSbm.setRefreshToken("1/A9smC2Y-21FBLOoU-SOmkWcVuk4ypiGqP7URnrjFjMk");
		googleCalendarSbm.setNextSyncToken("-BLANK-");
		googleCalendarSbm.setNextPageToken("CigKGjBxb3ZodWswMXB0Yzk5ZDc5bnQzczRoMmxzGAEggICA-M3XpogWGg0IABIAGKCSismV69gC");
		googleCalendarSbm.setLastQueryTimeMin("2018-02-20T08:38:13.000+07:00");
		ClientInfo ci = new ClientInfo();
		ci.setContactId(496);
		ContactItem contactItem = new ContactItem();
		contactItem.setClient(ci);
		when(contactItemService.load(any())).thenReturn(contactItem);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		SbmGoogleCalendar sbmGoogleCalendar = new SbmGoogleCalendar();
		sbmGoogleCalendar.setEventId("37ajh3jqsfqmrbo8i245iv18o9");
		sbmGoogleCalendar.setSbmId(24L);
		when(sbmCalendarService.queryWithIndex(any())).thenReturn(sbmGoogleCalendar);
		AwsProxyResponse response = calendarHanler.handleRequest(req, context);
		assertEquals(200,response.getStatusCode());
	}

}
