package com.tq.simplybook.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CancelInternalHandlerTest {
	ContactServiceInf csi = mock(ContactServiceInf.class);
	BookingServiceSbm bss = mock(BookingServiceSbm.class);
	TokenServiceSbm tss = mock(TokenServiceSbm.class);
	ContactItemService cis = mock(ContactItemService.class);
	ContactItem contactItem = new ContactItem();
	Env env = MockUtil.mockEnv();
	PayloadCallback payLoad = null;
	SbmClinikoSyncService scs = mock(SbmClinikoSyncService.class);
	SbmGoogleCalendarDbService sbmGoogleService = mock(SbmGoogleCalendarDbService.class);
	GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	ClinikoAppointmentService cas = mock(ClinikoAppointmentService.class);
	TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	ClinikoApiServiceBuilder clinikoApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	GoogleCalendarApiServiceBuilder googleApiBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	CancelInternalHandler handler = new CancelInternalHandler(env, tss, bss, csi, cis, scs, sbmGoogleService,
			googleCalendarService, tokenCalendarService, clinikoSyncToSbmService, clinikoApiServiceBuilder,
			googleApiBuilder);

	@Before
	public void init() throws SbmSDKException {
		payLoad = new PayloadCallback();
		payLoad.setBooking_id(5L);
		payLoad.setBooking_hash("784ee770544f77f25f5f713772cf6910");
		payLoad.setNotification_type("cancel");
		when(tss.getUserToken(any(), any(), any(), any())).thenReturn("token");
		BookingInfo bookingInfo = new BookingInfo().withClientEmail("suongpham5@gmail.com");
		bookingInfo.setEvent_id("2");
		bookingInfo.setUnit_id("1");
		when(bss.getBookingInfo(any(), any(), any(), any())).thenReturn(bookingInfo);
	}

	@Test
	public void testHandleCliniko() throws SbmSDKException, ClinikoSDKExeption, Exception {

		ClientInfo ci = new ClientInfo();
		ci.setContactId(448);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(contactItem);
		when(csi.update(any(), any(), any())).thenReturn(10);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);
		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setSbmId(5L);
		when(scs.load(any())).thenReturn(sbmCliniko);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey("api_key");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		ClinikoAppointmentService clinikoApptService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any())).thenReturn(clinikoApptService);
		when(scs.load(any())).thenReturn(sbmCliniko);
		when(clinikoApptService.deleteAppointment(any())).thenReturn(true);
		 handler.handle(payLoad);
	}

	@Test
	public void testHandleGoogleCalendar() throws SbmSDKException, GoogleApiSDKException, ClinikoSDKExeption {
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = new ArrayList<>();
		GoogleCalendarSbmSync googleSbm = new GoogleCalendarSbmSync();
		googleSbm.setRefreshToken("refresh_token");
		googleSbm.setGoogleCalendarId("google_calendar_id");
		googleCalendarSbmSync.add(googleSbm);
		when(googleCalendarService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("access_token");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		GoogleCalendarApiService googleApiService = mock(GoogleCalendarApiService.class);
		when(googleApiBuilder.build(any())).thenReturn(googleApiService);
		SbmGoogleCalendar sbmGoogleCalendar = new SbmGoogleCalendar();
		sbmGoogleCalendar.setEventId("event_id");
		when(sbmGoogleService.load(any())).thenReturn(sbmGoogleCalendar);
		when(googleApiService.deleteEvent(any(), any())).thenReturn(true);
		handler.handle(payLoad);
	}

	@Test
	public void testInfusionsoft() throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException, InfSDKExecption {
		ClientInfo ci = new ClientInfo();
		ci.setContactId(448);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(contactItem);
		when(csi.update(any(), any(), any())).thenReturn(10);
		when(csi.applyTag(any(), any(), any())).thenReturn(true);
		handler.handle(payLoad);

	}

}
