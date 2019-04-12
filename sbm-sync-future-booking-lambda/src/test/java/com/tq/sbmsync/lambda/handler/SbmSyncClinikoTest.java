package com.tq.sbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.AppointmentInfo;
import com.tq.cliniko.lambda.model.PatientDetail;
import com.tq.cliniko.lambda.model.Patients;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
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
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.service.TokenServiceSbm;

public class SbmSyncClinikoTest {
	private Env env = MockUtil.mockEnv();
	private Context context = mock(Context.class);
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private SbmClinikoSyncService sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private GoogleCalendarDbService googleCalendarDbService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);
	private ClinikoAppointmentService clinikoApiService = null;
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
	public void init() throws GoogleApiSDKException, SbmSDKException, ClinikoSDKExeption {
		List<SbmSyncFutureBookings> sbmSyncFutureBookings = Arrays.asList(

				new SbmSyncFutureBookings("1-7", "Api key", 1, 1527568729394L));
		when(sbmSyncFutureBookingService.querySyncStatus()).thenReturn(sbmSyncFutureBookings);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("Api Key", "test@gmail.com", "63791-98447", "1-7");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		when(tokenService.getUserToken(any(), any(), any(), any())).thenReturn("Sbm Token");
		ClinikoCompanyInfo clinikoCompanyValue = new ClinikoCompanyInfo(1000, 10000, "apiKey");
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyValue);
		clinikoApiService = mock(ClinikoAppointmentService.class);
		when(mockClinikoApiService.build(any())).thenReturn(clinikoApiService);
		Account account = new Account().withTimeZone("Melbourne").withCountry("Australia");
		Settings settingValue = new Settings(account);
		when(clinikoApiService.getAllSettings()).thenReturn(settingValue);
		AppointmentInfo apptInfo = new AppointmentInfo();
		apptInfo.setId(11111L);
		when(clinikoApiService.createAppointment(any())).thenReturn(apptInfo);
		AppointmentInfo result = new AppointmentInfo();
		result.setId(1L);
		when(clinikoApiService.createAppointment(any())).thenReturn(result);

	}

	@Test
	public void testNotExistPatient() throws SbmSDKException, ClinikoSDKExeption {
		List<GetBookingResp> bookingLists = Arrays.asList(new GetBookingResp("201", "2018-05-05 09:00:00",
				"2018-05-05 10:00:00", "jasmine", "", "jasmine@gmail.com", "0122336666"));
		SbmBookingList sbmBookingList = new SbmBookingList("1-7", bookingLists);
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		SbmCliniko sbmCliniko = null;
		when(sbmClinikoSyncService.load(any())).thenReturn(sbmCliniko);
		Patients patientValue = new Patients();
		List<PatientDetail> patientDetails = Arrays.asList();
		patientValue.setPatients(patientDetails);
		when(clinikoApiService.getPatient(any())).thenReturn(patientValue);
		PatientDetail patientDetail = new PatientDetail();
		patientDetail.setId(1);
		when(clinikoApiService.createPatient(any(), any(), any(), any())).thenReturn(patientDetail);

		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());

	}

	@Test
	public void testSyncBookingWithExistPatient() throws ClinikoSDKExeption {
		List<GetBookingResp> bookingLists = Arrays.asList(new GetBookingResp("201", "2018-05-05 09:00:00",
				"2018-05-05 10:00:00", "jasmine", "", "jasmine@gmail.com", "0122336666"));
		SbmBookingList sbmBookingList = new SbmBookingList("1-7", bookingLists);
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		SbmCliniko sbmCliniko = null;
		when(sbmClinikoSyncService.load(any())).thenReturn(sbmCliniko);
		List<PatientDetail> patients = new ArrayList<>();
		patients.add(new PatientDetail().withId(1));
		Patients patientValue = new Patients(patients);
		when(clinikoApiService.getPatient(any())).thenReturn(patientValue);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testHaveNoNewBooking() {
		List<GetBookingResp> bookingLists = Arrays.asList(new GetBookingResp("201", "2018-05-05 09:00:00",
				"2018-05-05 10:00:00", "jasmine", "", "jasmine@gmail.com", "0122336666"));
		SbmBookingList sbmBookingList = new SbmBookingList("1-7", bookingLists);
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setSbmId(1L);
		when(sbmClinikoSyncService.load(any())).thenReturn(sbmCliniko);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testEmptyBookingList() {
		List<GetBookingResp> bookingList = Arrays.asList();
		SbmBookingList sbmBookingList = new SbmBookingList("sbmId", bookingList);
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(null, context);
		assertEquals(200, response.getStatusCode());
	}

}
