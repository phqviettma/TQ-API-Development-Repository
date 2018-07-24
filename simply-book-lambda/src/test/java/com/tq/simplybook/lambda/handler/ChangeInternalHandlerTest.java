package com.tq.simplybook.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
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
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.resp.Location;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ChangeInternalHandlerTest {

	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private ContactItemService contactItemService = mock(ContactItemService.class);
	private BookingServiceSbm bookingService = mock(BookingServiceSbm.class);
	private Env env = MockUtil.mockEnv();
	private ObjectMapper mapper = new ObjectMapper();
	private SbmClinikoSyncService sbmClinikoService = mock(SbmClinikoSyncService.class);
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	private GoogleCalendarApiServiceBuilder googleApiBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private PayloadCallback payLoad = null;
	private BookingInfo bookingInfo = null;
	private CountryItemService countryItemService = mock(CountryItemService.class);
	private ContactServiceInf contactService = mock(ContactServiceInf.class);
	private ChangeInternalHandler changeHandler = new ChangeInternalHandler(env, bookingService, tokenService,
			countryItemService, contactService, contactItemService, clinikoSyncToSbmService, googleCalendarService,
			tokenCalendarService, clinikoApiServiceBuilder, sbmGoogleCalendarService, googleApiBuilder,
			sbmClinikoService);

	@Before
	public void init() throws SbmSDKException, Exception {
		payLoad = new PayloadCallback();
		payLoad.setBooking_id(105L);
		payLoad.setBooking_hash("f0d7728001d18b5f7463a8af809ac09f");
		payLoad.setNotification_type("change");
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
		when(bookingService.getBookingInfo(any(), any(), any(), any())).thenReturn(bookingInfo);
		String contactJsonPayLoad = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactItem-dummy.json"));
		ContactItem contactItem = mapper.readValue(contactJsonPayLoad, ContactItem.class);
		when(contactItemService.load(any())).thenReturn(contactItem);
	}

	@Test
	public void testSyncChangeToGoogle()
			throws GoogleApiSDKException, SbmSDKException, ClinikoSDKExeption, InfSDKExecption {
		when(clinikoSyncToSbmService.load(any())).thenReturn(null);
		List<GoogleCalendarSbmSync> googleCalendarLists = new ArrayList<>();
		GoogleCalendarSbmSync calendarSbm = new GoogleCalendarSbmSync();
		calendarSbm.setRefreshToken("refresh_token");
		googleCalendarLists.add(calendarSbm);
		when(googleCalendarService.queryEmail(any())).thenReturn(googleCalendarLists);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
		when(googleApiBuilder.build(any())).thenReturn(googleCalendarApiService);
		SbmGoogleCalendar sbmGoogle = new SbmGoogleCalendar();
		sbmGoogle.setEventId("event_id");
		GoogleCalendarSettingsInfo settingInfo = new GoogleCalendarSettingsInfo();
		settingInfo.setValue("Asia/SaiGon");
		when(googleCalendarApiService.getSettingInfo(any())).thenReturn(settingInfo);
		when(sbmGoogleCalendarService.load(any())).thenReturn(sbmGoogle);
		EventResp eventResp = new EventResp();
		when(googleCalendarApiService.updateEvent(any(), any(), any())).thenReturn(eventResp);
		changeHandler.handle(payLoad);
	}

	@Test
	public void testSyncCliniko() throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException, InfSDKExecption {
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey("api_key");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = new ArrayList<>();
		when(googleCalendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
		ClinikoAppointmentService clinikoApiService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any())).thenReturn(clinikoApiService);
		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setClinikoId(1L);
		when(sbmClinikoService.load(any())).thenReturn(sbmCliniko);
		Settings settingValue = new Settings(new Account().withCountry("Australia").withTimeZone("Melbourne"));
		when(clinikoApiService.getAllSettings()).thenReturn(settingValue);
		AppointmentInfo updatedAppointment = new AppointmentInfo();
		when(clinikoApiService.updateAppointment(any(), any())).thenReturn(updatedAppointment);

		changeHandler.handle(payLoad);
	}
}
