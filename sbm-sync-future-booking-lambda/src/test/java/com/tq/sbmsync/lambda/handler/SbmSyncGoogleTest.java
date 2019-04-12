package com.tq.sbmsync.lambda.handler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.resp.GetBookingResp;

public class SbmSyncGoogleTest {
	private Env env = MockUtil.mockEnv();
	private Context context = mock(Context.class);
	private SbmClinikoSyncService sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private GoogleCalendarDbService googleCalendarDbService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private ClinikoApiServiceBuilder mockClinikoApiService = mock(ClinikoApiServiceBuilder.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = mock(SbmSyncFutureBookingsService.class);
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarApiServiceBuilder mockApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private SbmBookingInfoService sbmBookingService = mock(SbmBookingInfoService.class);
	private SbmSyncClinikoHandler clinikoHandler = new SbmSyncClinikoHandler(clinikoSyncService, sbmClinikoSyncService,
			clinikoCompanyService, sbmSyncFutureBookingService, mockClinikoApiService, sbmListBookingService);
	private SbmSyncGCHandler gcHandler = new SbmSyncGCHandler(googleCalendarDbService, env, tokenCalendarService,
			sbmSyncFutureBookingService, sbmListBookingService, sbmGoogleCalendarService, mockApiServiceBuilder);
	private SbmSyncHandler sbmSyncHandler = new SbmSyncHandler(env, clinikoSyncService, sbmClinikoSyncService,
			googleCalendarDbService, tokenCalendarService, clinikoHandler, gcHandler, sbmSyncFutureBookingService, sbmListBookingService, sbmBookingService);

	@Before
	public void init() throws GoogleApiSDKException {
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockApiServiceBuilder.build(any())).thenReturn(googleCalendarApiService);
		GoogleCalendarSettingsInfo settingInfo = new GoogleCalendarSettingsInfo();
		settingInfo.setValue("Asia/Saigon");
		when(googleCalendarApiService.getSettingInfo(any())).thenReturn(settingInfo);
		EventResp eventResp = new EventResp();
		when(googleCalendarApiService.createEvent(any(), any())).thenReturn(eventResp);
		List<GetBookingResp> bookingLists = Arrays.asList(new GetBookingResp("201", "2018-05-05 09:00:00",
				"2018-05-05 10:00:00", "jasmine", "", "jasmine@gmail.com", "0122336666"));
		SbmBookingList sbmBookingList = new SbmBookingList("1-7", bookingLists);
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		List<SbmSyncFutureBookings> sbmSyncFutureBookings = Arrays.asList(

				new SbmSyncFutureBookings("1-7", null, "testingdev@tma.com.vn", 1, 1527568729394L));
		when(sbmSyncFutureBookingService.querySyncStatus()).thenReturn(sbmSyncFutureBookings);
		List<GoogleCalendarSbmSync> googleCalendarSbm = Arrays
				.asList(new GoogleCalendarSbmSync("1-7", "thuongsu@gmail.com", "primary", "Pham", "Suong",
						"1/Y482L09Olx9P7IjY4bKB2yT2wudQ3njk-jU8U_RkmhY", "suongpham53@gmail.com", "", ""));
		when(googleCalendarDbService.queryEmail(any())).thenReturn(googleCalendarSbm);
	}

	@Test
	public void testHaveNewBooking() throws GoogleApiSDKException {

		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("dfb4a2e724f99ad8a31d6bd77e0cb917", "thuongsu@gmail.com",
				"63241-97566", "2-19");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(48933484, 63791, "apiKey");
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo);

		SbmCliniko sbmCliniko = new SbmCliniko(34L, 111000L, 0, "", "");
		when(sbmClinikoSyncService.load(any())).thenReturn(sbmCliniko);

		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testHaveNoNewBooking() {
		SbmGoogleCalendar sbmGoogleCalendar = new SbmGoogleCalendar();
		sbmGoogleCalendar.setSbmId(2L);
		when(sbmGoogleCalendarService.load(any())).thenReturn(sbmGoogleCalendar);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testEmptyBookingList() {
		SbmBookingList sbmBookingList = new SbmBookingList();
		sbmBookingList.setSbmId("sbmId");
		List<GetBookingResp> bookingList = Arrays.asList();
		sbmBookingList.setBookingList(bookingList);
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testHandleException() {
		List<SbmSyncFutureBookings> sbmSyncFutureBookings = Arrays.asList(

				new SbmSyncFutureBookings("1-7", null, null, 1, 1527568729394L));
		when(sbmSyncFutureBookingService.querySyncStatus()).thenReturn(sbmSyncFutureBookings);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertThat("Error, can not sync to cliniko/google", is("Error, can not sync to cliniko/google"));
		assertEquals(500, response.getStatusCode());
	}

}
