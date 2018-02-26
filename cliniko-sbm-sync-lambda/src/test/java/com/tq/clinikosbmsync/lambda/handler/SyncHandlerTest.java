package com.tq.clinikosbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.context.SimplyBookClinikoMapping;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class SyncHandlerTest {
	private Env m_env = MockUtil.mockEnv();
	private Context m_context = mock(Context.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private ClinikoAppointmentService clinikoService = new ClinikiAppointmentServiceImpl(m_env.getClinikoApiKey());
	private LatestClinikoApptService latestService = mock(LatestClinikoApptService.class);
	private LatestClinikoApptServiceWrapper clinikoApptService = mock(LatestClinikoApptServiceWrapper.class);
	private SpecialdayServiceSbm specialdayService = new SpecialdayServiceSbmImpl();
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private SimplyBookClinikoMapping sbmClinikoMapping = new SimplyBookClinikoMapping(m_env);
	private SbmBreakTimeManagement sbmTimeManagement = new SbmBreakTimeManagement();

	@Test
	public void testHandleClinikoSbmSync() {

		SyncHandler handler = new SyncHandler(m_env, amazonDynamoDB, clinikoService, latestService, clinikoApptService,
				specialdayService, tokenService, sbmClinikoMapping, sbmTimeManagement);
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_appointment.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		LatestClinikoAppts dpAppts = new LatestClinikoAppts();
		dpAppts.setLatestUpdateTime("2017-01-25T19:21:49Z");
		Set<Long> created = new HashSet<>();
		created.add(96011766L);
		dpAppts.setCreated(created);
		Set<Long> removed = new HashSet<>();
		removed.add(11L);
		dpAppts.setRemoved(removed);
		when(clinikoApptService.load()).thenReturn(dpAppts);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(clinikoApptService).put(any(LatestClinikoAppts.class));
		AwsProxyResponse respone = handler.handleRequest(req, m_context);
		assertEquals(200, respone.getStatusCode());
	}
	public static void main(String[] args) {
		
	}
	
}
