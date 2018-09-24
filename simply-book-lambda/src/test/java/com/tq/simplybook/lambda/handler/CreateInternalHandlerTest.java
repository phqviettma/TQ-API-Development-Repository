package com.tq.simplybook.lambda.handler;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.PatientDetail;
import com.tq.cliniko.lambda.model.Patients;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmBookingInfoService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
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

public class CreateInternalHandlerTest {

	private ContactServiceInf csi = mock(ContactServiceInf.class);

	private TokenServiceSbm tss = mock(TokenServiceSbm.class);
	private ContactItemService cis = mock(ContactItemService.class);
	private ContactItem contactItem = new ContactItem();
	private BookingServiceSbm bss = mock(BookingServiceSbm.class);
	private Env env = MockUtil.mockEnv();
	private ClinikoAppointmentService clinikoApptService = null;
	private SbmClinikoSyncService scs = mock(SbmClinikoSyncService.class);
	private SbmGoogleCalendarDbService sbmGoogleService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenGoogleService = mock(TokenGoogleCalendarService.class);
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	private GoogleCalendarApiServiceBuilder googleApiBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private PayloadCallback payLoad = null;
	private BookingInfo bookingInfo = null;
	private SbmBookingInfoService sbmBookingInfoService = mock(SbmBookingInfoService.class);
	private CountryItemService countryItemService = mock(CountryItemService.class);
	private CreateInternalHandler handler = new CreateInternalHandler(env, tss, bss, csi, cis, scs,
			googleCalendarService, sbmGoogleService, tokenGoogleService, clinikoSyncToSbmService, clinikoCompanyService,
			clinikoApiServiceBuilder, googleApiBuilder, countryItemService, sbmBookingInfoService);

	@Before
	public void init() throws SbmSDKException, ClinikoSDKExeption {
		payLoad = new PayloadCallback();
		payLoad.setBooking_id(105L);
		payLoad.setBooking_hash("f0d7728001d18b5f7463a8af809ac09f");
		payLoad.setNotification_type("create");
		when(tss.getUserToken(any(), any(), any(), any())).thenReturn("token");
		clinikoApptService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any())).thenReturn(clinikoApptService);
		Settings settingValue = new Settings();
		Account account = new Account().withCountry("Australia").withTimeZone("Melbourne");
		settingValue.setAccount(account);
		when(clinikoApptService.getAllSettings()).thenReturn(settingValue);
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(1000, 10000);
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo);
		AppointmentInfo apptInfo = new AppointmentInfo();
		apptInfo.setId(199000L);
		when(clinikoApptService.createAppointment(any())).thenReturn(apptInfo);
		bookingInfo = new BookingInfo().withClientEmail("suongpham03@gmail.com").withClientName("suong pham")
				.withClientPhone("0122553333").withClientId(97).withRecordDate("2018-06-22 16:36:43");
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
	public void testHandleExcuteCliniko()
			throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException, InfSDKExecption {
		when(cis.load(any())).thenReturn(null);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("ff997f7d491b555f227262870a2717c1", "chicanh@gmail.com",
				"62052-95260", "1-12");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		List<GoogleCalendarSbmSync> sbmGoogle = new ArrayList<>();
		when(googleCalendarService.queryEmail(any())).thenReturn(sbmGoogle);
		when(tss.getUserToken(any(), any(), any(), any())).thenReturn("token");
		List<PatientDetail> patients = Arrays.asList(new PatientDetail().withId(1222));
		Patients patientValue = new Patients(patients);
		when(clinikoApptService.getPatient(any())).thenReturn(patientValue);

		when(csi.addWithDupCheck(any(), any(), any())).thenReturn(518);
		when(csi.update(any(), any(), any())).thenReturn(518);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);

		when(countryItemService.queryCountryCode(any())).thenReturn("AU");
		handler.handle(payLoad);
	}

	@Test
	public void testExcuteInfusionsoft() throws SbmSDKException, InfSDKExecption {
		ClientInfo ci = new ClientInfo();
		ci.setEmail("truequit-testing@gmail.com");
		ci.setContactId(518);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(contactItem);
		when(csi.update(any(), any(), any())).thenReturn(122);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);
		when(countryItemService.queryCountryCode(any())).thenReturn("AU");
		boolean excuted = handler.executeWithInfusionSoft(payLoad, bookingInfo, contactItem.getClient().getContactId());
		assertTrue(excuted);
	}
	
	@Test
	public void testPatientNotExist() throws ClinikoSDKExeption, SbmSDKException, GoogleApiSDKException {
		Patients patient = new Patients();
		List<PatientDetail> patients = Arrays.asList();
		patient.setPatients(patients);
		when(clinikoApptService.getPatient(any())).thenReturn(patient);
		PatientDetail patientDetail = new PatientDetail();
		patientDetail.setId(1);
		ClinikoSbmSync clinikoSbm = new ClinikoSbmSync("apiKey", "email", "1-1", "1-1");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbm);
		when(clinikoApptService.createPatient(any(), any(), any(), any())).thenReturn(patientDetail);
		handler.handle(payLoad);
	}

	@Test
	public void testHandleExcuteGoogle() throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException {
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = new ArrayList<>();
		GoogleCalendarSbmSync googleSbm = new GoogleCalendarSbmSync();
		googleSbm.setRefreshToken("refresh_token");
		googleSbm.setGoogleCalendarId("google_calendar_id");
		googleCalendarSbmSync.add(googleSbm);
		when(googleCalendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
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
		handler.handle(payLoad);
	}

}
