package com.tq.calendar.lambda.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.calendar.lambda.context.Env;
import com.tq.calendar.lambda.handler.RegisterHandler;
import com.tq.calendar.lambda.model.UserInfoResp;
import com.tq.common.lambda.dynamodb.model.CalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.CalendarSbmService;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.impl.UnitServiceSbmImpl;
import com.tq.simplybook.service.TokenServiceSbm;
import com.tq.simplybook.service.UnitServiceSbm;

public class RegisterHandlerTest {
	private CalendarSbmService calendarService = mock(CalendarSbmService.class);
	private Env mockedeEnv = MockUtil.mockEnv();
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private UnitServiceSbm unitService = new UnitServiceSbmImpl();
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);

	@Test
	public void testRegisterHandler() {
		Env.mock(mockedeEnv);
		
		RegisterHandler handler = new RegisterHandler(mockedeEnv,amazonDynamoDB,unitService,tokenService,calendarService);

		   String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("user_info.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(calendarService).put(any(CalendarSbmSync.class));
		handler.handleRequest(req, m_context);
	}
}
