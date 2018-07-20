package com.tq.cliniko.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.context.ClinikoEnv;
import com.tq.cliniko.lambda.model.AppointmentType;
import com.tq.cliniko.lambda.model.Businesses;
import com.tq.cliniko.lambda.model.BusinessesInfo;
import com.tq.cliniko.lambda.model.ClinikoAppointmentType;
import com.tq.cliniko.lambda.model.Links;
import com.tq.cliniko.lambda.model.Practitioner;
import com.tq.cliniko.lambda.model.PractitionersInfo;
import com.tq.cliniko.lambda.model.Self;
import com.tq.cliniko.lambda.model.User;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.model.ClinikoCompanyInfo;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmBookingList;
import com.tq.common.lambda.dynamodb.model.SbmSyncFutureBookings;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmListBookingService;
import com.tq.common.lambda.dynamodb.service.SbmSyncFutureBookingsService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.resp.GetBookingResp;
import com.tq.simplybook.resp.UnitProviderInfo;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoRegisterHandlerTest {
	private ClinikoEnv mockedeEnv = MockUtil.mockEnv();
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private SbmUnitService unitService = mock(SbmUnitService.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	private ClinikoItemService clinikoItemService = mock(ClinikoItemService.class);
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private SbmSyncFutureBookingsService sbmSyncBookingService = mock(SbmSyncFutureBookingsService.class);
	private BookingServiceSbm bookingService = mock(BookingServiceSbm.class);
	private SbmListBookingService sbmListBookingService = mock(SbmListBookingService.class);
	private ClinikoApiServiceBuilder mockApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	private CheckingHandler checkingHandler = new CheckingHandler(clinikoSyncToSbmService);
	private ClinikoConnectHandler connectHandler = new ClinikoConnectHandler(mockedeEnv, unitService, tokenService,
			clinikoSyncToSbmService, clinikoItemService, clinikoCompanyService, sbmSyncBookingService, bookingService, sbmListBookingService, mockApiServiceBuilder);
	private ClinikoDisconnectHandler disconnectHandler = new ClinikoDisconnectHandler(clinikoSyncToSbmService,
			clinikoItemService, sbmSyncBookingService, sbmListBookingService);

	@Test
	public void testCheckingHandler() {

		ClinikoRegisterHandler handler = new ClinikoRegisterHandler(mockedeEnv, amazonDynamoDB, unitService,
				tokenService, clinikoSyncToSbmService, connectHandler, disconnectHandler, checkingHandler);
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_check_info.json"));
		req.setBody(json);
		ClinikoSbmSync clinikoSbmSync = null;
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		when(clinikoSyncToSbmService.queryEmail(any())).thenReturn(clinikoSbmSync);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(clinikoSyncToSbmService).put(any(ClinikoSbmSync.class));
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());

	}
	@Test
	public void testConnectHandler() throws SbmSDKException, ClinikoSDKExeption {
		ClinikoRegisterHandler handler = new ClinikoRegisterHandler(mockedeEnv, amazonDynamoDB, unitService,
				tokenService, clinikoSyncToSbmService, connectHandler, disconnectHandler, checkingHandler);
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_connect_info.json"));
		req.setBody(json);
		ClinikoSbmSync clinikoSbmSync = null;
		when(clinikoSyncToSbmService.load(any())).thenReturn(clinikoSbmSync);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(clinikoSyncToSbmService).put(any(ClinikoSbmSync.class));
		when(tokenService.getUserToken(any(), any(), any(),any())).thenReturn("Sbm Token");
		List<UnitProviderInfo> unitProviderInfo = new ArrayList<>();
		UnitProviderInfo unitInfo = new UnitProviderInfo().withEmail("tmatesting@gmail.com");
		Map<String, Object> event_map = new HashMap<>();
		event_map.put("1", null);
		unitInfo.setEvent_map(event_map );
		unitInfo.setId("7");
		unitProviderInfo.add(unitInfo );
		when(unitService.getUnitList(any(), any(), any(), any(), any(), any())).thenReturn(unitProviderInfo );
		when(clinikoSyncToSbmService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		ClinikoAppointmentService clinikoAptService = mock(ClinikoAppointmentService.class);
		when(mockApiServiceBuilder.build(any())).thenReturn(clinikoAptService );
		PractitionersInfo practitionerInfo = new PractitionersInfo();
		List<Practitioner> practitioners = Arrays.asList(new Practitioner().withPractitionerId(1));
		practitionerInfo.setPractitioners(practitioners );
		when(clinikoAptService.getPractitioner(any())).thenReturn(practitionerInfo );
		User userInfo = new User();
		userInfo.setEmail("tmatesting@gmail.com");
		when(clinikoAptService.getAuthenticateUser()).thenReturn(userInfo );
		BusinessesInfo businessInfo = new BusinessesInfo();
		List<Businesses> businesses = new ArrayList<>();
		Businesses business =new Businesses().withPractitioner(new Links().withLink(new Self().withSelf("Link")));
		business.setId(1000);
		businesses.add(business );
		businessInfo.setBusinesses(businesses );
		when(clinikoAptService.getListBusinesses()).thenReturn(businessInfo );
		PractitionersInfo practitionersInfo = new PractitionersInfo();
		practitionersInfo.setPractitioners(practitioners);
		when(clinikoAptService.getBusinessPractitioner(any())).thenReturn(practitionersInfo);
		ClinikoCompanyInfo clinikoCompanyInfo = null;
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo );
		ClinikoAppointmentType clinikoApptType = new ClinikoAppointmentType();
		List<AppointmentType> appointment_types = Arrays.asList(new AppointmentType().withId(1));
		clinikoApptType.setAppointment_types(appointment_types );
		when(clinikoAptService.getAppointmentType(any())).thenReturn(clinikoApptType );
		List<GetBookingResp> getBookingResp = Arrays.asList(new GetBookingResp());
		when(bookingService.getBookings(any(), any(), any(), any())).thenReturn(getBookingResp );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
	@Test
	public void testDisconnectHandler() {
		ClinikoRegisterHandler handler = new ClinikoRegisterHandler(mockedeEnv, amazonDynamoDB, unitService,
				tokenService, clinikoSyncToSbmService, connectHandler, disconnectHandler, checkingHandler);
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_disconnect_info.json"));
		req.setBody(json);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		when(clinikoSyncToSbmService.queryEmail(any())).thenReturn(clinikoSbmSync );
		ClinikoSyncStatus clinikoSyncStatus  = new ClinikoSyncStatus();
		when(clinikoItemService.load(any())).thenReturn(clinikoSyncStatus  );
		SbmSyncFutureBookings futureBookings = new SbmSyncFutureBookings();
		when(sbmSyncBookingService.load(any())).thenReturn(futureBookings);
		SbmBookingList sbmBookingList = new SbmBookingList();
		when(sbmListBookingService.load(any())).thenReturn(sbmBookingList );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
}
