package com.tq.simplybook.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.PatientDetail;
import com.tq.cliniko.lambda.model.Patients;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.model.SbmGoogleCalendar;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
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
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.resp.BookingInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class CreateInternalHandlerTest {

	private ContactServiceInf csi = new ContactServiceImpl();

	private TokenServiceSbm tss = mock(TokenServiceSbm.class);
	private ContactItemService cis = mock(ContactItemService.class);
	private ContactItem contactItem = new ContactItem();
	private BookingServiceSbm bss = mock(BookingServiceSbm.class);
	private Env env = MockUtil.mockEnv();
	private SbmClinikoSyncService scs = mock(SbmClinikoSyncService.class);
	private SbmGoogleCalendarDbService sbmGoogleService = mock(SbmGoogleCalendarDbService.class);
	private GoogleCalendarDbService googleCalendarService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenGoogleService = new TokenGoogleCalendarImpl();
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	private CreateInternalHandler handler = new CreateInternalHandler(env, tss, bss, csi, cis, scs,
			googleCalendarService, sbmGoogleService, tokenGoogleService, clinikoSyncToSbmService,
			clinikoCompanyService, clinikoApiServiceBuilder);

	@Test
	public void test() throws SbmSDKException, ClinikoSDKExeption, GoogleApiSDKException {
		ClientInfo ci = new ClientInfo();
		ci.setEmail("truequit-testing@gmail.com");
		ci.setContactId(518);
		contactItem.setClient(ci);
		when(cis.load(any())).thenReturn(null);
		PayloadCallback payLoad = new PayloadCallback();
		payLoad.setBooking_id(94L);
		payLoad.setBooking_hash("732acb8efee6b74dbacebe5d45da28e3");
		payLoad.setNotification_type("create");
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(scs).put(any(SbmCliniko.class));
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {

				return null;
			}

		}).when(sbmGoogleService).put(any(SbmGoogleCalendar.class));
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("ff997f7d491b555f227262870a2717c1", "chicanh@gmail.com",
				"62052-95260", "1-12");
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		when(googleCalendarService.queryEmail(any())).thenReturn(null);
		when(tss.getUserToken(any(), any(), any(), any())).thenReturn("token");
		ClinikoAppointmentService clinikoApptService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any())).thenReturn(clinikoApptService );
		Settings settingValue = new Settings();
		Account account = new Account().withCountry("Australia").withTimeZone("Melbourne");
		settingValue.setAccount(account);
		when(clinikoApptService.getAllSettings()).thenReturn(settingValue );
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo(1000, 10000);
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo );
		List<PatientDetail> patients = Arrays.asList(new PatientDetail().withId(1222));
		Patients patientValue = new Patients(patients );
		when(clinikoApptService.getPatient(any())).thenReturn(patientValue );
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setClient_email("suongpham03@gmail.com");
		bookingInfo.setEvent_id("6");
		bookingInfo.setUnit_id("1");
		bookingInfo.setStart_date_time("2018-01-01");
		bookingInfo.setEnd_date_time("2018-01-16");
		when(bss.getBookingInfo(any(), any(), any(), any())).thenReturn(bookingInfo );
		
		handler.handle(payLoad);
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
