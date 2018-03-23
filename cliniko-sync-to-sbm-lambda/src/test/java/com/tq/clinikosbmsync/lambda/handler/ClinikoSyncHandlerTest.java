package com.tq.clinikosbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoAppointmentWrapper;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.model.LatestClinikoAppts;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoAppointmentService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.impl.SpecialdayServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoSyncHandlerTest {
	private Env m_env = MockUtil.mockEnv();
	private Context m_context = mock(Context.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private LatestClinikoApptService latestService = mock(LatestClinikoApptService.class);
	private LatestClinikoApptServiceWrapper clinikoApptService = mock(LatestClinikoApptServiceWrapper.class);
	private SpecialdayServiceSbm specialdayService = new SpecialdayServiceSbmImpl();
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private SbmBreakTimeManagement sbmTimeManagement = new SbmBreakTimeManagement();
	private LatestClinikoAppointmentService clinikoAppointmentService = mock(LatestClinikoAppointmentService.class);
	private LatestClinikoAppointmentWrapper latestClinikoWrapper = mock(LatestClinikoAppointmentWrapper.class);
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);

	ClinikoSyncHandler handler = new ClinikoSyncHandler(m_env, amazonDynamoDB, specialdayService, tokenService,
			sbmTimeManagement, clinikoAppointmentService, latestClinikoWrapper, clinikoSyncService);

	@Test
	public void testSyncHandler() {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_appointment.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(clinikoApptService).put(any(LatestClinikoAppts.class));
		List<LatestClinikoAppointment> latestClinikoAppt = new ArrayList<>();
		Set<Long> created = new HashSet<>();
		created.add(96011766L);
		Set<Long> removed = new HashSet<>();
		removed.add(11L);
		LatestClinikoAppointment dpAppt1 = new LatestClinikoAppointment(created, removed, "2018-01-25T19:21:49Z",
				"e795b490bf01c39fec5936169c923bb8");
		LatestClinikoAppointment dpAppt2 = new LatestClinikoAppointment(created, removed, "2018-03-25T19:21:49Z",
				"e795b490bf01c39fec5936169c923bb8");
		latestClinikoAppt.add(dpAppt1);
		latestClinikoAppt.add(dpAppt2);
		when(clinikoAppointmentService.queryItem()).thenReturn(latestClinikoAppt);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync("e795b490bf01c39fec5936169c923bb8", "chicanh@gmail.com", "58837-89589", 6, 1);
		when(clinikoSyncService.load(any())).thenReturn(clinikoSbmSync );
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
}
