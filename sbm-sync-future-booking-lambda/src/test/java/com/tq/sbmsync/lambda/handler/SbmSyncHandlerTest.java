package com.tq.sbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.TokenServiceSbm;

public class SbmSyncHandlerTest {
	private Env env = MockUtil.mockEnv();
	private Context context = mock(Context.class);
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private SbmClinikoSyncService sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private GoogleCalendarDbService googleCalendarDbService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);
	private BookingServiceSbmImpl bookingSbmService = mock(BookingServiceSbmImpl.class);
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private ClinikoApiServiceBuilder mockClinikoApiService = mock(ClinikoApiServiceBuilder.class);
	private SbmSyncFutureBookingsService sbmSyncFutureBookingService = mock(SbmSyncFutureBookingsService.class);
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarApiServiceBuilder mockApiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private SbmSyncClinikoHandler clinikoHandler = new SbmSyncClinikoHandler(clinikoSyncService, sbmClinikoSyncService, clinikoCompanyService, sbmSyncFutureBookingService, mockClinikoApiService, sbmListBookingService);
	private SbmSyncGCHandler gcHandler = new SbmSyncGCHandler(googleCalendarDbService, env, tokenCalendarService,
			sbmSyncFutureBookingService, sbmListBookingService, sbmGoogleCalendarService, mockApiServiceBuilder);
	private SbmSyncHandler sbmSyncHandler = new SbmSyncHandler(env, clinikoSyncService, sbmClinikoSyncService, googleCalendarDbService, tokenCalendarService, clinikoHandler, gcHandler,
			sbmSyncFutureBookingService);

	@Test
	public void testGoogleHandler() throws GoogleApiSDKException {
		AwsProxyRequest req = new AwsProxyRequest();
		List<SbmSyncFutureBookings> sbmSyncFutureBookings = Arrays.asList(

				new SbmSyncFutureBookings("1-7", null, "testingdev@tma.com.vn", 1, 1527568729394L));
		when(sbmSyncFutureBookingService.querySyncStatus()).thenReturn(sbmSyncFutureBookings);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("dfb4a2e724f99ad8a31d6bd77e0cb917", "thuongsu@gmail.com",
				"63241-97566", "2-19");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(48933484, 63791, 284190);
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo);
		List<GoogleCalendarSbmSync> googleCalendarSbm = Arrays.asList(new GoogleCalendarSbmSync("1-7",
				"thuongsu@gmail.com", "primary", "Pham", "Suong", "1/Y482L09Olx9P7IjY4bKB2yT2wudQ3njk-jU8U_RkmhY", "suongpham53@gmail.com", "", ""));
		when(googleCalendarDbService.queryEmail(any())).thenReturn(googleCalendarSbm);
		SbmCliniko sbmCliniko = new SbmCliniko(34L, 111000L, 0, "", "");
		when(sbmClinikoSyncService.load(any())).thenReturn(sbmCliniko);
		List<GetBookingResp> bookingLists = Arrays.asList(new GetBookingResp("201","2018-05-05 09:00:00","2018-05-05 10:00:00","jasmine","","jasmine@gmail.com"));
		SbmBookingList sbmBookingList = new SbmBookingList("1-7", bookingLists );
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(mockApiServiceBuilder.build(any())).thenReturn(googleCalendarApiService);
		GoogleCalendarSettingsInfo settingInfo = new GoogleCalendarSettingsInfo();
		settingInfo.setValue("Asia/Saigon");
		when(googleCalendarApiService.getSettingInfo(any())).thenReturn(settingInfo );
		EventResp eventResp = new EventResp();
		when(googleCalendarApiService.createEvent(any(), any())).thenReturn(eventResp );
		AwsProxyResponse response = sbmSyncHandler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}
	@Test
	public void testClinikoHandler() throws SbmSDKException, ClinikoSDKExeption {
		AwsProxyRequest req = new AwsProxyRequest();
		List<SbmSyncFutureBookings> sbmSyncFutureBookings = Arrays.asList(

				new SbmSyncFutureBookings("1-7", "Api key", 1, 1527568729394L));
		when(sbmSyncFutureBookingService.querySyncStatus()).thenReturn(sbmSyncFutureBookings);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("Api Key", "test@gmail.com", "63791-98447", "1-7");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbmSync );
		when(tokenService.getUserToken(any(), any(), any(), any())).thenReturn("Sbm Token");
		ClinikoCompanyInfo clinikoCompanyValue = new ClinikoCompanyInfo(1000, 10000, 10000);
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyValue );
		ClinikoAppointmentService clinikoApiService = mock(ClinikoAppointmentService.class);
		when(mockClinikoApiService.build(any())).thenReturn(clinikoApiService );
		SbmCliniko sbmCliniko = null ;
		when(sbmClinikoSyncService.load(any())).thenReturn(sbmCliniko );
		List<GetBookingResp> bookingInfo = Arrays.asList(new GetBookingResp("201","2018-05-05 09:00:00","2018-05-05 10:00:00","jasmine","","jasmine@gmail.com"));
		when(bookingSbmService.getBookings(any(), any(), any(), any())).thenReturn(bookingInfo  );
		Account account = new Account().withTimeZone("Melbourne").withCountry("Australia");
		Settings settingValue = new Settings(account );
		when(clinikoApiService.getAllSettings()).thenReturn(settingValue );
		AppointmentInfo apptInfo = new AppointmentInfo();
		apptInfo.setId(11111L);
		when(clinikoApiService.createAppointment(any())).thenReturn(apptInfo );
		AwsProxyResponse response = sbmSyncHandler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
		
	}
}
