package com.tq.gcsyncsbm.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.tq.googlecalendar.context.Env;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class GCSyncHandlerTest {
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private Env env = MockUtil.mockEnv();
	private Context context = mock(Context.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private SpecialdayServiceSbm specialdayService = new SpecialdayServiceSbmImpl();
	private SbmBreakTimeManagement sbmBreakTimeManagement = new SbmBreakTimeManagement();
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private SbmUnitService unitService = new SbmUnitServiceImpl();
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private ContactServiceInf contactInfService = new ContactServiceImpl();
	private SbmGoogleCalendarDbService sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private BookingServiceSbm bookingService = new BookingServiceSbmImpl();
	private CreateGoogleEventHandler createHandler = new CreateGoogleEventHandler(env, tokenService, specialdayService,
			sbmBreakTimeManagement, sbmGoogleCalendarService, unitService);
	private DeleteGoogleEventHandler deleteHandler = new DeleteGoogleEventHandler(env, tokenService,
			googleCalendarService, specialdayService, sbmBreakTimeManagement, contactItemService, contactInfService,
			sbmCalendarService, bookingService, unitService);
	CalendarSyncHandler handler = new CalendarSyncHandler(env, amazonDynamoDB, googleCalendarService, specialdayService,
			createHandler, deleteHandler, unitService, modifiedChannelService, sbmCalendarService);

	@Test
	public void testGCSyncHandler() {
		AwsProxyRequest req = new AwsProxyRequest();
		GoogleCalendarSbmSync googleCalendarSbm = new GoogleCalendarSbmSync();
		googleCalendarSbm.setSbmId("2-4");
		googleCalendarSbm.setEmail("tmatesting@gmail.com");
		googleCalendarSbm.setGoogleEmail("jayparkjay34@gmail.com");
		googleCalendarSbm.setRefreshToken("");
		googleCalendarSbm.setNextSyncToken("CPjh7vfKn9oCEPjh7vfKn9oCGAU=");
		googleCalendarSbm.setNextPageToken("-BLANK-");
		ClientInfo ci = new ClientInfo();
		ci.setContactId(496);
		ContactItem contactItem = new ContactItem();
		contactItem.setClient(ci);
		when(contactItemService.load(any())).thenReturn(contactItem);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		SbmGoogleCalendar sbmGoogleCalendar = new SbmGoogleCalendar();
		sbmGoogleCalendar.setEventId("3se4p41fa9h2ftejaj7uthqd1f");
		sbmGoogleCalendar.setAgent("google");
		sbmGoogleCalendar.setFlag(1);
		sbmGoogleCalendar.setSbmId(24L);
		when(sbmCalendarService.queryWithIndex(any())).thenReturn(sbmGoogleCalendar);
		List<GCModifiedChannel> listModifiedItem = new ArrayList<>();
		listModifiedItem.add(new GCModifiedChannel("2-4",1, 0));
		when(modifiedChannelService.queryItem()).thenReturn(listModifiedItem );
		AwsProxyResponse response = handler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}
}
