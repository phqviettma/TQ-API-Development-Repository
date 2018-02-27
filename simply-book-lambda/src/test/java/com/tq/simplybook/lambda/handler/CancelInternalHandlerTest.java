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
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.SimplyBookClinikoMapping;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CancelInternalHandlerTest {
	ContactServiceInf csi = new ContactServiceImpl();
	BookingServiceSbm bss = new BookingServiceSbmImpl();
	TokenServiceSbm tss = new TokenServiceImpl();
	ContactItemService cis = mock(ContactItemService.class);
	ContactItem contactItem = new ContactItem();
	Env env = MockUtil.mockEnv();
	SbmClinikoSyncService scs = mock(SbmClinikoSyncService.class);
	LatestClinikoApptServiceWrapper lcs = mock(LatestClinikoApptServiceWrapper.class);
	SbmGoogleCalendarDbService sbmGoogleService = mock(SbmGoogleCalendarDbService.class);
	GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	ClinikoAppointmentService cas = new ClinikiAppointmentServiceImpl(env.getClinikoApiKey());
	SimplyBookClinikoMapping scm = new SimplyBookClinikoMapping(env);
	TokenGoogleCalendarImpl tokenCalendarService = new TokenGoogleCalendarImpl();
	@Test
	public void test() throws SbmSDKException, ClinikoSDKExeption, Exception {

		ClientInfo ci = new ClientInfo();
		ci.setContactId(448);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(contactItem);
		CancelInternalHandler handler = new CancelInternalHandler(env, tss, bss, csi, cis, scs, lcs, cas, scm,
				sbmGoogleService, googleCalendarService, tokenCalendarService);
		PayloadCallback payLoad = new PayloadCallback();
		payLoad.setBooking_id(5L);
		payLoad.setBooking_hash("784ee770544f77f25f5f713772cf6910");
		payLoad.setNotification_type("cancel");
		SbmCliniko sbmCliniko = new SbmCliniko();
		sbmCliniko.setSbmId(5L);
		when(scs.load(any())).thenReturn(sbmCliniko);
		LatestClinikoAppts latest = new LatestClinikoAppts();
		when(lcs.load()).thenReturn(latest);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(lcs).put(any(LatestClinikoAppts.class));
		handler.handle(payLoad);
	}

}
