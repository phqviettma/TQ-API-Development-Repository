package com.tq.simplybook.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.SimplyBookClinikoMapping;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandlerTest {
	private ContactServiceInf csi = new ContactServiceImpl();

	private TokenServiceSbm tss = new TokenServiceImpl();
	private ContactItemService cis = mock(ContactItemService.class);
	private ContactItem contactItem = new ContactItem();
	private BookingServiceSbm bss = new BookingServiceSbmImpl();
	private Env env = MockUtil.mockEnv();
	private SimplyBookClinikoMapping scm = new SimplyBookClinikoMapping(env);
	private SbmClinikoSyncService scs = mock(SbmClinikoSyncService.class);
	private LatestClinikoApptServiceWrapper lcs = mock(LatestClinikoApptServiceWrapper.class);
	private ClinikoAppointmentService cas = new ClinikiAppointmentServiceImpl(env.getClinikoApiKey());
	private SbmGoogleCalendarDbService sbmGoogleService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenGoogleService = new TokenGoogleCalendarImpl();
	private CreateInternalHandler handler = new CreateInternalHandler(env, tss, bss, csi, cis, scm, scs, lcs, cas,
			googleCalendarService, sbmGoogleService, tokenGoogleService);

	//@Test
	public void test() throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException {
		ClientInfo ci = new ClientInfo();
		ci.setEmail("truequit-testing@gmail.com");
		ci.setContactId(518);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(contactItem);
		PayloadCallback payLoad = new PayloadCallback();
		payLoad.setBooking_id(94L);
		payLoad.setBooking_hash("732acb8efee6b74dbacebe5d45da28e3");
		payLoad.setNotification_type("create");
		when(lcs.load()).thenReturn(new LatestClinikoAppts());
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(lcs).put(any(LatestClinikoAppts.class));
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(scs).put(any(SbmCliniko.class));
		handler.handle(payLoad);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {

				return null;
			}

		}).when(sbmGoogleService).put(any(SbmGoogleCalendar.class));
	}

	@Test
	public void testExcuteInfusionsoft() throws SbmSDKException {
		ClientInfo ci = new ClientInfo();
		ci.setEmail("truequit-testing@gmail.com");
		ci.setContactId(518);
		contactItem.setClient(ci);

		when(cis.load(any())).thenReturn(contactItem);
		PayloadCallback payLoad = new PayloadCallback();
		payLoad.setBooking_id(105L);
		payLoad.setBooking_hash("f0d7728001d18b5f7463a8af809ac09f");
		payLoad.setNotification_type("create");
		String token = tss.getUserToken(env.getSimplyBookCompanyLogin(), env.getSimplyBookUser(),
				env.getSimplyBookServiceUrlLogin(), env.getSimplyBookServiceUrlLogin());
		BookingInfo bookingInfo = bss.getBookingInfo(env.getSimplyBookCompanyLogin(),
				env.getSimplyBookServiceUrlLogin(), token, payLoad.getBooking_id());
		handler.executeWithInfusionSoft(payLoad, bookingInfo, contactItem);
	}

}
