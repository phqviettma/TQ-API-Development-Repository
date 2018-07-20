package com.tq.clinikosbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikoApiServiceBuilder;
import com.tq.cliniko.lambda.model.Account;
import com.tq.cliniko.lambda.model.AppointmentsInfo;
import com.tq.cliniko.lambda.model.Settings;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.req.FromDate;
import com.tq.simplybook.req.ToDate;
import com.tq.simplybook.resp.Breaktime;
import com.tq.simplybook.resp.UnitWorkingTime;
import com.tq.simplybook.resp.WorkTimeSlot;
import com.tq.simplybook.resp.WorkingTime;
import com.tq.simplybook.resp.WorksDayInfoResp;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoSyncHandlerTest {
	private Env m_env = MockUtil.mockEnv();
	private ObjectMapper mapper = new ObjectMapper();
	private static final String API_KEY = "";
	private Context m_context = mock(Context.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private SpecialdayServiceSbm specialdayService = mock(SpecialdayServiceSbm.class);
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private SbmBreakTimeManagement sbmTimeManagement = mock(SbmBreakTimeManagement.class);
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);
	private ClinikoItemService clinikoItemService = mock(ClinikoItemService.class);
	private SbmClinikoSyncService sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private SbmUnitService unitService = mock(SbmUnitService.class);
	private BookingServiceSbm bookingService = mock(BookingServiceSbm.class);
	private ClinikoAppointmentService clinikoApptService = null;
	private ClinikoApiServiceBuilder clinikoApiServiceBuilder = mock(ClinikoApiServiceBuilder.class);
	ClinikoSyncHandler handler = new ClinikoSyncHandler(m_env, amazonDynamoDB, specialdayService, tokenService,
			sbmTimeManagement, clinikoSyncService, clinikoItemService, sbmClinikoSyncService, unitService,
			bookingService, clinikoApiServiceBuilder);

	@Before
	public void init() throws ClinikoSDKExeption, SbmSDKException {
	
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync(API_KEY, "suongpham53@gmail.com", "62052-95259", "1-5");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		clinikoApptService = mock(ClinikoAppointmentService.class);
		when(clinikoApiServiceBuilder.build(any(String.class))).thenReturn(clinikoApptService);
		Settings settingValue = new Settings(new Account().withCountry("Australia").withTimeZone("Melbourne"));
		when(clinikoApptService.getAllSettings()).thenReturn(settingValue);
		Map<String, UnitWorkingTime> unitWorkingTime = new HashMap<>();
		Map<String, WorkingTime> workingTimeMaps = new HashMap<>();
		WorkingTime workingTimeValue = new WorkingTime("09:00:00", "18:00:00");
		workingTimeMaps.put("5", workingTimeValue);
		UnitWorkingTime workingTime = new UnitWorkingTime("2018-06-19", workingTimeMaps);
		unitWorkingTime.put("2018-06-19", workingTime);
		when(unitService.getUnitWorkDayInfo(any(), any(), any(), any(), any(), any())).thenReturn(unitWorkingTime);
		Map<String, WorksDayInfoResp> workDayInfos = new HashMap<>();
		WorksDayInfoResp worksDayInfo = new WorksDayInfoResp();
		worksDayInfo.setDate("2018-06-19");
		Set<WorkTimeSlot> workTimeSlot = new HashSet<>();
		workTimeSlot.add(new WorkTimeSlot("09:00:00", "10:00:00"));
		worksDayInfo.setInfo(workTimeSlot);
		workDayInfos.put("2018-06-19", worksDayInfo);
		FromDate fromDate = new FromDate("2018-06-18", "09:00:00");
		ToDate toDate = new ToDate("2018-06-19", "10:00:00");
		when(specialdayService.getWorkDaysInfo("companyLogin", "endpoint", "token", 1, 5, fromDate, toDate))
				.thenReturn(workDayInfos);
		Set<Breaktime> newBreakTime = new HashSet<>();
		newBreakTime.add(new Breaktime("09:00:00", "10:00:00"));
		Map<String, WorksDayInfoResp> workDayInfo = new HashMap<>();
		WorksDayInfoResp workdayResp = new WorksDayInfoResp();
		workdayResp.setDate("2018-06-19");
		workdayResp.setInfo(workTimeSlot);
		workDayInfo.put("2018-06-19", workdayResp);
		when(sbmTimeManagement.addBreakTime("companyLogin", "endpoint", "token", 1, 5, "09:00:00", "18:00:00",
				"2018-06-19", newBreakTime, workDayInfo)).thenReturn(true);
	}

	@Test
	public void testSyncCreateAppointmentWithTimeMin() throws ClinikoSDKExeption, Exception {
		List<ClinikoSyncStatus> clinikoSync = new ArrayList<>();
		clinikoSync.add(new ClinikoSyncStatus(ClinikoSyncStatus.CHECK_KEY, API_KEY, 190005555L, null));
		when(clinikoItemService.queryIndex()).thenReturn(clinikoSync);
		String appointments = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko-appointment.json"));

		AppointmentsInfo appts = mapper.readValue(appointments, AppointmentsInfo.class);

		when(clinikoApptService.getAppointments(any(), any(), any())).thenReturn(appts);

		when(sbmClinikoSyncService.queryIndex(any())).thenReturn(null);
		
		
		when(clinikoApptService.getDeletedAppointments("", 0, 0)).thenReturn(appts);
		AwsProxyResponse response = handler.handleRequest(null, m_context);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void testSyncNewestCreateAppt() throws Exception {
		List<ClinikoSyncStatus> clinikoSync = new ArrayList<>();
		clinikoSync.add(new ClinikoSyncStatus(ClinikoSyncStatus.CHECK_KEY, API_KEY, 190005555L, "2018-06-19T04:18:37Z"));
		when(clinikoItemService.queryIndex()).thenReturn(clinikoSync);
		String appointments = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko-newest-appointment.json"));

		AppointmentsInfo appts = mapper.readValue(appointments, AppointmentsInfo.class);
		when(clinikoApptService.getNewestAppointment("2018-06-19T04:18:37Z", 20, 95259)).thenReturn(appts);
		AwsProxyResponse response = handler.handleRequest(null, m_context);
		assertEquals(200, response.getStatusCode());
	}
	@Test
	@Ignore
	public void testSyncCancelledAppt() throws Exception {
		List<ClinikoSyncStatus> clinikoSync = new ArrayList<>();
		clinikoSync.add(new ClinikoSyncStatus(ClinikoSyncStatus.CHECK_KEY, API_KEY, 190005555L, "2018-06-19T04:18:37Z"));
		when(clinikoItemService.queryIndex()).thenReturn(clinikoSync);
		String appointments = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cancelled_appointment.json"));
		AppointmentsInfo appts = mapper.readValue(appointments, AppointmentsInfo.class);
		when(clinikoApptService.getCancelAppointments("2018-06-19T04:18:37Z", 0, 95259)).thenReturn(appts);
		when(clinikoApptService.getNewestAppointment("2018-06-19T04:18:37Z", 20, 95259)).thenReturn(appts);
		AwsProxyResponse response = handler.handleRequest(null, m_context);
		assertEquals(200, response.getStatusCode());
	}
}
