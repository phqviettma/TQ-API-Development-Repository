package com.tq.gcsyncsbm.lambda.handler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.common.lambda.dynamodb.model.GCModifiedChannel;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
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
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class GCSyncCreateHandlerTest {
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private Env env = MockUtil.mockEnv();
	private Context context = mock(Context.class);
	private ObjectMapper mapper = new ObjectMapper();
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private SpecialdayServiceSbm specialdayService = mock(SpecialdayServiceSbm.class);
	private SbmBreakTimeManagement sbmBreakTimeManagement = mock(SbmBreakTimeManagement.class);
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private SbmUnitService unitService = mock(SbmUnitService.class);
	private GoogleCalendarModifiedSyncService modifiedChannelService = mock(GoogleCalendarModifiedSyncService.class);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private ContactServiceInf contactInfService = mock(ContactServiceInf.class);
	private SbmGoogleCalendarDbService sbmCalendarService = mock(SbmGoogleCalendarDbService.class);
	private BookingServiceSbm bookingService = mock(BookingServiceSbm.class);
	private GoogleCalendarApiService googleApiService = null;
	private CalendarEvents googleCalendarEvents = null;
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private CreateGoogleEventHandler createHandler = new CreateGoogleEventHandler(env, tokenService, specialdayService,
			sbmBreakTimeManagement, sbmGoogleCalendarService, unitService);
	private DeleteGoogleEventHandler deleteHandler = new DeleteGoogleEventHandler(env, tokenService,
			googleCalendarService, specialdayService, sbmBreakTimeManagement, contactItemService, contactInfService,
			sbmCalendarService, bookingService, unitService);
	CalendarSyncHandler handler = new CalendarSyncHandler(env, amazonDynamoDB, googleCalendarService, specialdayService,
			createHandler, deleteHandler, unitService, modifiedChannelService, sbmCalendarService, apiServiceBuilder,
			tokenCalendarService);

	@Before
	public void mockForAllCase() throws SbmSDKException, Exception {

		List<GCModifiedChannel> listModifiedItem = new ArrayList<>();
		listModifiedItem.add(new GCModifiedChannel());
		when(modifiedChannelService.queryCheckStatusIndex()).thenReturn(listModifiedItem);

		googleApiService = mock(GoogleCalendarApiService.class);
		when(apiServiceBuilder.build(anyString())).thenReturn(googleApiService);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		String events = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("google_events.json"));
		googleCalendarEvents = mapper.readValue(events, CalendarEvents.class);
		Map<String, UnitWorkingTime> unitWorkingTime = new HashMap<>();
		Map<String, WorkingTime> workingTimeMaps = new HashMap<>();
		WorkingTime workingTimeValue = new WorkingTime("09:00:00", "18:00:00");
		workingTimeMaps.put("23", workingTimeValue);
		UnitWorkingTime workingTime = new UnitWorkingTime("2010-06-13", workingTimeMaps);
		unitWorkingTime.put("2010-06-13", workingTime);
		when(unitService.getUnitWorkDayInfo(any(), any(), any(), any(), any(), any())).thenReturn(unitWorkingTime);
		Map<String, WorksDayInfoResp> workDayInfos = new HashMap<>();
		WorksDayInfoResp worksDayInfo = new WorksDayInfoResp();
		worksDayInfo.setDate("2010-06-13");
		Set<WorkTimeSlot> workTimeSlot = new HashSet<>();
		workTimeSlot.add(new WorkTimeSlot("09:00:00", "10:00:00"));
		worksDayInfo.setInfo(workTimeSlot);
		workDayInfos.put("2010-06-13", worksDayInfo);
		FromDate fromDate = new FromDate("2010-06-13", "09:00:00");
		ToDate toDate = new ToDate("2010-06-13", "10:00:00");
		when(specialdayService.getWorkDaysInfo("companyLogin", "endpoint", "token", 2, 23, fromDate, toDate))
				.thenReturn(workDayInfos);
		when(tokenService.getUserToken(anyString(), anyString(), anyString(), anyString())).thenReturn("token");
	}

	@Test

	public void testEventWithNoSyncToken() throws GoogleApiSDKException, Exception {
		String bodyReq = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_no_sync_token.json"));
		GoogleCalendarSbmSync googleCalendarSbm = mapper.readValue(bodyReq, GoogleCalendarSbmSync.class);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		GoogleCalendarSettingsInfo settingInfo = new GoogleCalendarSettingsInfo();
		settingInfo.setValue("Australia/Perth");
		when(googleApiService.getSettingInfo(anyString())).thenReturn(settingInfo);

		when(googleApiService.getEventWithoutToken(any(), anyString(), anyString(), anyString()))
				.thenReturn(googleCalendarEvents);
		when(sbmCalendarService.queryWithIndex(any())).thenReturn(null);

		AwsProxyResponse response = handler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test

	public void testHaveNextPageToken() throws Exception {
		String bodyReq = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_have_query_time.json"));
		GoogleCalendarSbmSync googleCalendarSbm = mapper.readValue(bodyReq, GoogleCalendarSbmSync.class);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		String events = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_event.json"));
		googleCalendarEvents = mapper.readValue(events, CalendarEvents.class);
		when(googleApiService.queryNextEventWithTimeMin(any(), anyString(), anyString(), anyString()))
				.thenReturn(googleCalendarEvents);
		AwsProxyResponse response = handler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test

	public void testErrorWhenNextPageUnset() throws Exception {
		String bodyReq = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_have_no_next_page.json"));
		GoogleCalendarSbmSync googleCalendarSbm = mapper.readValue(bodyReq, GoogleCalendarSbmSync.class);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		AwsProxyResponse response = handler.handleRequest(null, context);
		assertThat("Illegal state, LastQueryTimeMin is set while NextPageToken is unset",
				is("Illegal state, LastQueryTimeMin is set while NextPageToken is unset"));
		assertEquals(500, response.getStatusCode());
	}

	@Test

	public void testCreateEventWithSyncToken() throws Exception {
		String bodyReq = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_have_sync_token.json"));
		GoogleCalendarSbmSync googleCalendarSbm = mapper.readValue(bodyReq, GoogleCalendarSbmSync.class);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		String events = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_event_lastpage.json"));
		googleCalendarEvents = mapper.readValue(events, CalendarEvents.class);
		when(googleApiService.getEventWithNextSyncToken(any(), any(), any())).thenReturn(googleCalendarEvents);
		AwsProxyResponse response = handler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test

	public void testCreateEvWithSyncTokennNextPage() throws Exception {
		String bodyReq = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_have_sync_token_n_nextp.json"));
		GoogleCalendarSbmSync googleCalendarSbm = mapper.readValue(bodyReq, GoogleCalendarSbmSync.class);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		when(googleApiService.getEventWithNextPageToken(any(), any(), any(), any())).thenReturn(googleCalendarEvents);
		AwsProxyResponse response = handler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
		
		// invalid next page token
		when(googleApiService.getEventWithNextPageToken(any(), any(), any(), any())).thenReturn(null);
		response = handler.handleRequest(null, context);
		assertEquals(null, googleCalendarSbm.getNextPageToken());
		assertEquals(null, googleCalendarSbm.getNextSyncToken());
		assertEquals(null, googleCalendarSbm.getLastQueryTimeMin());
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testCreateEventFullDay() throws Exception {
		String bodyReq = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_have_sync_token.json"));
		GoogleCalendarSbmSync googleCalendarSbm = mapper.readValue(bodyReq, GoogleCalendarSbmSync.class);
		when(googleCalendarService.load(any())).thenReturn(googleCalendarSbm);
		String events = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("gc_event_fullday.json"));
		googleCalendarEvents = mapper.readValue(events, CalendarEvents.class);
		when(googleApiService.getEventWithNextSyncToken(any(), any(), any())).thenReturn(googleCalendarEvents);
		when(specialdayService.changeWorkDay(anyString(), anyString(), anyString(), any())).thenReturn(true);
		AwsProxyResponse response = handler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
		
		//invalid next sync token
		when(googleApiService.getEventWithNextSyncToken(any(), any(), any())).thenReturn(null);
		response = handler.handleRequest(null, context);
		assertEquals(null, googleCalendarSbm.getNextPageToken());
		assertEquals(null, googleCalendarSbm.getNextSyncToken());
		assertEquals(null, googleCalendarSbm.getLastQueryTimeMin());
		assertEquals(200, response.getStatusCode());
	}

}
