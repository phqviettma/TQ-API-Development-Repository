package com.tq.cliniko.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoAppointmentService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoRegisterHandlerTest {
	private Env mockedeEnv = MockUtil.mockEnv();
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private SbmUnitService unitService = new SbmUnitServiceImpl();
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);
	private LatestClinikoAppointmentService latestClinikoService = mock(LatestClinikoAppointmentService.class);
	private ClinikoConnectHandler connectHandler = new ClinikoConnectHandler(mockedeEnv, unitService, tokenService,
			clinikoSyncToSbmService, latestClinikoService);
	private ClinikoDisconnectHandler disconnectHandler = new ClinikoDisconnectHandler(clinikoSyncToSbmService, latestClinikoService);

	@Before
	public void init() {
		unitService = new SbmUnitServiceImpl();
	}

	@Test
	public void testHandler() {

		ClinikoRegisterHandler handler = new ClinikoRegisterHandler(mockedeEnv, amazonDynamoDB, unitService,
				tokenService, clinikoSyncToSbmService, connectHandler, disconnectHandler);
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
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());

	}
}
