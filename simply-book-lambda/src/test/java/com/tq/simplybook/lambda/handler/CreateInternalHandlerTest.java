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
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.model.PayloadCallback;
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
	private CreateInternalHandler handler = new CreateInternalHandler(env, tss, bss, csi, cis, scm, scs, lcs,cas);

	@Test
	public void test() throws SbmSDKException, ClinikoSDKExeption {
		ClientInfo ci = new ClientInfo();
		ci.setEmail("thuongsu@gmail.com");
		ci.setContactId(448);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(contactItem);
		PayloadCallback payLoad = new PayloadCallback();
		payLoad.setBooking_id(5L);
		payLoad.setBooking_hash("784ee770544f77f25f5f713772cf6910");
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
	}

	@Test
	public void testExcuteInfusionsoft() throws SbmSDKException {
		ClientInfo ci = new ClientInfo();
		ci.setContactId(448);
		contactItem.setClient(ci);

		when(cis.load(any())).thenReturn(contactItem);
		PayloadCallback payLoad = new PayloadCallback();
		payLoad.setBooking_id(5L);
		payLoad.setBooking_hash("784ee770544f77f25f5f713772cf6910");
		payLoad.setNotification_type("create");
		handler.executeWithInfusionSoft(payLoad);
	}

}
