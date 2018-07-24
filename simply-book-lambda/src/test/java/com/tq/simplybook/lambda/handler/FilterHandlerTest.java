package com.tq.simplybook.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.EventResp;
import com.tq.googlecalendar.resp.GoogleCalendarSettingsInfo;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.Location;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class FilterHandlerTest {
	private ContactServiceInf csi = mock(ContactServiceInf.class);

	private TokenServiceSbm tss = mock(TokenServiceSbm.class);
	private ContactItemService cis = mock(ContactItemService.class);
	private BookingServiceSbm bss = mock(BookingServiceSbm.class);
	private Env env = MockUtil.mockEnv();
	private SbmClinikoSyncService scs = mock(SbmClinikoSyncService.class);
	private SbmGoogleCalendarDbService sbmGoogleService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenGoogleService = mock(TokenGoogleCalendarService.class);
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	private GoogleCalendarApiServiceBuilder googleApiBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private CountryItemService countryItemService = mock(CountryItemService.class);
	private BookingInfo bookingInfo = null;
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private CreateInternalHandler createHandler = new CreateInternalHandler(env, tss, bss, csi, cis, scs,
			googleCalendarService, sbmGoogleService, tokenGoogleService, clinikoSyncToSbmService, clinikoCompanyService,
			clinikoApiServiceBuilder, googleApiBuilder, countryItemService);
	private CancelInternalHandler cancelHandler = new CancelInternalHandler(env, tss, bss, csi, cis, scs,
			sbmGoogleService, googleCalendarService, tokenCalendarService, clinikoSyncToSbmService,
			clinikoApiServiceBuilder, googleApiBuilder);
	private ChangeInternalHandler changeHandler = new ChangeInternalHandler(env, bss, tss, countryItemService, csi, cis,
			clinikoSyncToSbmService, googleCalendarService, tokenGoogleService, clinikoApiServiceBuilder,
			sbmGoogleService, googleApiBuilder, scs);

	@Before
	public void init() throws SbmSDKException {
		bookingInfo = new BookingInfo().withClientEmail("suongpham03@gmail.com").withClientName("suong pham")
				.withClientPhone("0122553333").withClientId(97).withRecordDate("2018-06-22 16:36:43");
		bookingInfo.setId("1");
		bookingInfo.setEvent_id("6");
		bookingInfo.setUnit_id("1");
		bookingInfo.setUnit_name("Suong pham");
		bookingInfo.setStart_date_time("2018-06-27 10:00:00");
		bookingInfo.setEnd_date_time("2018-06-27 11:00:00");
		Location location = new Location();
		location.setCountry_id("AU");
		bookingInfo.setLocation(location);
		when(bss.getBookingInfo(any(), any(), any(), any())).thenReturn(bookingInfo);
	}

	@Test
	public void testHandleCreateClinikoRequest() throws SbmSDKException, InfSDKExecption {
		FilterEventHandler handler = new FilterEventHandler(env, csi, bss, createHandler, cancelHandler, changeHandler);
		Context context = mock(Context.class);
		String jsonString = JsonUtils.getJsonString(
				this.getClass().getClassLoader().getResourceAsStream("create_booking_info_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		when(csi.addWithDupCheck(any(), any(), any())).thenReturn(518);
		when(csi.update(any(), any(), any())).thenReturn(518);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(1000, 10000);
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo);
		AwsProxyResponse response = handler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testHandleCancelClinikoRequest() throws SbmSDKException, InfSDKExecption {
		FilterEventHandler handler = new FilterEventHandler(env, csi, bss, createHandler, cancelHandler, changeHandler);
		Context context = mock(Context.class);
		String jsonString = JsonUtils.getJsonString(
				this.getClass().getClassLoader().getResourceAsStream("cancel_booking_info_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
	
		when(csi.addWithDupCheck(any(), any(), any())).thenReturn(518);
		when(csi.update(any(), any(), any())).thenReturn(518);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(1000, 10000);
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey("api_key");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setSbmId(5L);
		sbmCliniko.setClinikoId(1L);
		when(scs.load(any())).thenReturn(sbmCliniko);
		ClinikoAppointmentService clinikoAppointmentService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any())).thenReturn(clinikoAppointmentService);
		AwsProxyResponse response = handler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testOtherNotificationType() {
		FilterEventHandler handler = new FilterEventHandler(env, csi, bss, createHandler, cancelHandler, changeHandler);
		Context context = mock(Context.class);
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("notify_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		AwsProxyResponse response = handler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testGoogleCreateHandleRequest() throws SbmSDKException, InfSDKExecption, GoogleApiSDKException {
		FilterEventHandler handler = new FilterEventHandler(env, csi, bss, createHandler, cancelHandler, changeHandler);
		Context context = mock(Context.class);
		String jsonString = JsonUtils.getJsonString(
				this.getClass().getClassLoader().getResourceAsStream("create_booking_info_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		when(csi.addWithDupCheck(any(), any(), any())).thenReturn(518);
		when(csi.update(any(), any(), any())).thenReturn(518);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);
		when(clinikoCompanyService.load(any())).thenReturn(null);
		GoogleCalendarSbmSync googleCalendarSync = new GoogleCalendarSbmSync();
		googleCalendarSync.setRefreshToken("refresh_token");
		googleCalendarSync.setGoogleCalendarId("google_calendar_id");
		List<GoogleCalendarSbmSync> googleSbmSyncs = Arrays.asList(googleCalendarSync);
		when(googleCalendarService.queryEmail(any())).thenReturn(googleSbmSyncs);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		GoogleCalendarApiService googleApiService = mock(GoogleCalendarApiService.class);
		when(googleApiBuilder.build(any())).thenReturn(googleApiService);
		when(tokenGoogleService.getToken(any())).thenReturn(tokenResp);
		GoogleCalendarSettingsInfo googleSettingInfo = new GoogleCalendarSettingsInfo();
		googleSettingInfo.setValue("Asia/Saigon");
		when(googleApiService.getSettingInfo(any())).thenReturn(googleSettingInfo);
		EventResp eventResp = new EventResp();
		eventResp.setId("event_id");
		when(googleApiService.createEvent(any(), any())).thenReturn(eventResp);
		when(countryItemService.queryCountryCode(any())).thenReturn("AU");
		AwsProxyResponse response = handler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testChangeHandler() throws ClinikoSDKExeption, InfSDKExecption {
		FilterEventHandler handler = new FilterEventHandler(env, csi, bss, createHandler, cancelHandler, changeHandler);
		Context context = mock(Context.class);
		String jsonString = JsonUtils.getJsonString(
				this.getClass().getClassLoader().getResourceAsStream("change_booking_info_payload.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		ContactItem contactItem = new ContactItem();
		contactItem.setEmail("email");
		ClientInfo client = new ClientInfo();
		client.setContactId(1);
		contactItem.setClient(client );
		when(cis.load(any())).thenReturn(contactItem );
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey("api_key");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync );
		ClinikoAppointmentService clinikoApptService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any())).thenReturn(clinikoApptService );
		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setClinikoId(1L);
		when(scs.load(any())).thenReturn(sbmCliniko );
		Settings settingValue = new Settings(new Account().withCountry("Australia").withTimeZone("Melbourne"));
		when(clinikoApptService.getAllSettings()).thenReturn(settingValue );
		AppointmentInfo updatedAppt = new AppointmentInfo();
		when(clinikoApptService.updateAppointment(any(), any())).thenReturn(updatedAppt );
		List<GoogleCalendarSbmSync> googleSbmSync = new ArrayList<>();
		when(googleCalendarService.queryEmail(any())).thenReturn(googleSbmSync);
		when(csi.update(any(), any(), any())).thenReturn(1);
		AwsProxyResponse response = handler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}
}
