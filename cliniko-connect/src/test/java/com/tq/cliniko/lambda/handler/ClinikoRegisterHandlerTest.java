package com.tq.cliniko.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.impl.UnitServiceSbmImpl;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.service.UnitServiceSbm;

public class ClinikoRegisterHandlerTest {
	private Env mockedeEnv = MockUtil.mockEnv();
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private UnitServiceSbm unitService = null;
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);
	private ClinikoSyncToSbmService clinikoSyncToSbmService = mock(ClinikoSyncToSbmService.class);

	@Before
	public void init() {
		unitService = new UnitServiceSbmImpl();
	}

	@Test
	public void testHandler() {

		ClinikoRegisterHandler handler = new ClinikoRegisterHandler(mockedeEnv, amazonDynamoDB, unitService,
				tokenService, clinikoSyncToSbmService);
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
		handler.handleRequest(req, m_context);

	}
}