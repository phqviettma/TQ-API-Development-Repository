package com.tq.cliniko.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

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
	private CheckingHandler checkingHandler = new CheckingHandler(clinikoSyncToSbmService, mockApiServiceBuilder);
	private ClinikoConnectHandler connectHandler = new ClinikoConnectHandler(mockedeEnv, unitService, tokenService,
			clinikoSyncToSbmService, clinikoItemService, clinikoCompanyService, sbmSyncBookingService, bookingService, sbmListBookingService);
	private ClinikoDisconnectHandler disconnectHandler = new ClinikoDisconnectHandler(clinikoSyncToSbmService,
			clinikoItemService, sbmSyncBookingService, sbmListBookingService, clinikoCompanyService);
	private ClinikoGetDataHandler getDataHandler = new ClinikoGetDataHandler(clinikoSyncToSbmService, mockApiServiceBuilder);
	private ClinikoResyncHandler resyncHandler = new ClinikoResyncHandler(clinikoSyncToSbmService, clinikoItemService);

	private ClinikoRegisterHandler initHandler() {
		return new ClinikoRegisterHandler(mockedeEnv, amazonDynamoDB, unitService,
				tokenService, clinikoSyncToSbmService, connectHandler, disconnectHandler, checkingHandler, getDataHandler, resyncHandler);
	}
	@Test
	public void testCheckingHandler_disconnected() {
		ClinikoRegisterHandler handler = initHandler();
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
		assertTrue(response.getBody().contains("disconnected"));
	}
	
	@Test
	public void testCheckingHandler_connected() throws ClinikoSDKExeption {
		ClinikoRegisterHandler handler = initHandler();
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_check_info.json"));
		req.setBody(json);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey("apiKey");
		clinikoSbmSync.setClinikoId("businessId-practitionerId");
		when(clinikoSyncToSbmService.queryEmail(any())).thenReturn(clinikoSbmSync);
		ClinikoAppointmentService clinikoService = mock(ClinikoAppointmentService.class);
		when(clinikoService.getAuthenticateUser()).thenReturn(new User());
		Businesses business = new Businesses();
		business.setBusiness_name("Businesses Name");
		when(clinikoService.getBusinessById("businessId")).thenReturn(business);
		when(mockApiServiceBuilder.build("apiKey")).thenReturn(clinikoService);
		
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
		assertTrue(response.getBody().contains("connected"));
		assertTrue(response.getBody().contains("Businesses Name"));
	}
	
	@Test
	public void testCheckingHandler_invalidApiKey() throws ClinikoSDKExeption {
		ClinikoRegisterHandler handler = initHandler();
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_check_info.json"));
		req.setBody(json);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey("apiKey");
		when(clinikoSyncToSbmService.queryEmail(any())).thenReturn(clinikoSbmSync);
		ClinikoAppointmentService clinikoService = mock(ClinikoAppointmentService.class);
		when(clinikoService.getAuthenticateUser()).thenReturn(null);
		when(mockApiServiceBuilder.build("apiKey")).thenReturn(clinikoService);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		verify(clinikoSyncToSbmService).delete(clinikoSbmSync);
		assertEquals(200, response.getStatusCode());
		assertTrue(response.getBody().contains("disconnected"));
	}
	
	@Test
	public void testConnectHandler() throws SbmSDKException, ClinikoSDKExeption {
		ClinikoRegisterHandler handler = initHandler();
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
		List<GetBookingResp> getBookingResp = Arrays.asList(new GetBookingResp());
		when(bookingService.getBookings(any(), any(), any(), any())).thenReturn(getBookingResp );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
	@Test
	public void testDisconnectHandler() {
		ClinikoRegisterHandler handler = initHandler();
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
		ClinikoCompanyInfo clinikoCompanyInfo = new ClinikoCompanyInfo();
		when(clinikoCompanyService.load(any())).thenReturn(clinikoCompanyInfo);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void testGetDataHandler() throws ClinikoSDKExeption {
		ClinikoRegisterHandler handler = initHandler();
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_get_data_info.json"));
		req.setBody(json);
		ClinikoSbmSync clinikoSbmSync = null;
		when(clinikoSyncToSbmService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		ClinikoAppointmentService clinikoAptService = mock(ClinikoAppointmentService.class);
		when(mockApiServiceBuilder.build(any())).thenReturn(clinikoAptService);
		when(clinikoAptService.getAuthenticateUser()).thenReturn(new User());
		
		PractitionersInfo allPractitioners = new PractitionersInfo();
		Practitioner practitioner1 = new Practitioner();
		practitioner1.setId(1);
		Practitioner practitioner2 = new Practitioner();
		practitioner2.setId(2);
		Practitioner practitioner3 = new Practitioner();
		practitioner3.setId(2);
		List<Practitioner> listPractitioner1 = new ArrayList<Practitioner>();
		listPractitioner1.add(practitioner1);
		listPractitioner1.add(practitioner2);
		listPractitioner1.add(practitioner3);
		allPractitioners.setPractitioners(listPractitioner1);
		
		BusinessesInfo allBusinesses = new BusinessesInfo();
		Businesses business1 = new Businesses();
		business1.setId(1);
		business1.setBusiness_name("BUS1");
		business1.setCountry("AU");
		List<Integer> appointmentTypesList1 = new ArrayList<Integer>();
		appointmentTypesList1.add(1);
		appointmentTypesList1.add(2);
		appointmentTypesList1.add(3);
		business1.setAppointment_type_ids(appointmentTypesList1);
		business1.setPractitioners(new Links().withLink(new Self().withSelf("http://business1")));
		Businesses business2 = new Businesses();
		business2.setId(1);
		business2.setBusiness_name("BUS2");
		business2.setCountry("AU");
		List<Integer> appointmentTypesList2 = new ArrayList<Integer>();
		appointmentTypesList2.add(1);
		appointmentTypesList2.add(2);
		appointmentTypesList2.add(3);
		business2.setAppointment_type_ids(appointmentTypesList2);
		business2.setPractitioners(new Links().withLink(new Self().withSelf("http://business2")));
		List<Businesses> listBusiness = new ArrayList<Businesses>();
		listBusiness.add(business1);
		listBusiness.add(business2);
		allBusinesses.setBusinesses(listBusiness);
		
		when(clinikoAptService.getAllPractitioner()).thenReturn(allPractitioners);
		when(clinikoAptService.getListBusinesses()).thenReturn(allBusinesses);
		
		PractitionersInfo allPractitionersOfBusinesses = new PractitionersInfo();
		Practitioner practitioner4 = new Practitioner();
		practitioner4.setId(1);
		Practitioner practitioner5 = new Practitioner();
		practitioner5.setId(2);
		Practitioner practitioner6 = new Practitioner();
		practitioner6.setId(2);
		List<Practitioner> listPractitioner2 = new ArrayList<Practitioner>();
		listPractitioner2.add(practitioner4);
		listPractitioner2.add(practitioner5);
		listPractitioner2.add(practitioner6);
		allPractitionersOfBusinesses.setPractitioners(listPractitioner2);
		
		when(clinikoAptService.getBusinessPractitioner(any())).thenReturn(allPractitionersOfBusinesses);
		
		AppointmentType appointmentType1 = new AppointmentType();
		appointmentType1.setId(1);
		AppointmentType appointmentType2 = new AppointmentType();
		appointmentType2.setId(1);
		AppointmentType appointmentType3 = new AppointmentType();
		appointmentType3.setId(1);
		ClinikoAppointmentType allAppointmentTypesOfPractitioner = new ClinikoAppointmentType();
		List<AppointmentType> listAppointmentType = new ArrayList<AppointmentType>();
		listAppointmentType.add(appointmentType1);
		listAppointmentType.add(appointmentType2);
		listAppointmentType.add(appointmentType3);
		allAppointmentTypesOfPractitioner.setAppointment_types(listAppointmentType);
		
		when(clinikoAptService.getAppointmentType(any())).thenReturn(allAppointmentTypesOfPractitioner);
		
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void testResyncHandler() {
		String email = "tmatesting@gmail.com";
		String apiKey = "apiKey";
		ClinikoRegisterHandler handler = initHandler();
		AwsProxyRequest req = new AwsProxyRequest();
		String json = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_resync_info.json"));
		req.setBody(json);
		// case 1
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertTrue(response.getBody().contains("The practitioner email "+email+" does not exist"));
		
		// case 2
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync();
		clinikoSbmSync.setApiKey(apiKey);
		when(clinikoSyncToSbmService.queryEmail(email)).thenReturn(clinikoSbmSync);
		response = handler.handleRequest(req, m_context);
		assertTrue(response.getBody().contains("The provided API Key does not exist"));
		
		// case 3
		ClinikoSyncStatus clinikoSyncStatus = new ClinikoSyncStatus();
		when(clinikoItemService.queryWithIndex(apiKey)).thenReturn(clinikoSyncStatus);
		response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
		
	}
}
