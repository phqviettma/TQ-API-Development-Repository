package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.googlecalendar.lambda.context.Env;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.simplybook.impl.SbmUnitServiceImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.TokenServiceSbm;

public class RegisterHandlerTest {
	private GoogleCalendarDbService calendarService = mock(GoogleCalendarDbService.class);
	private static Env mockedeEnv = MockUtil.mockEnv();
	private static TokenServiceSbm tokenService = new TokenServiceImpl();
	private static SbmUnitService unitService = new SbmUnitServiceImpl();
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private Context m_context = mock(Context.class);
	private ContactServiceInf contactService = new ContactServiceImpl();
	private ContactItemService contactItemService = mock(ContactItemService.class);

	@Test
	public void testRegisterHandler() {
		Env.mock(mockedeEnv);

		RegisterHandler handler = new RegisterHandler(mockedeEnv, amazonDynamoDB, unitService, tokenService,
				calendarService, contactService, contactItemService);

		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("user_info.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		//GoogleCalendarSbmSync googleCalendarSbmSync = new GoogleCalendarSbmSync("1-7", "phamthanhcute11@gmail.com","phamthanhcute11@gmail.com", "suong", "pham","", "1/A9smC2Y-21FBLOoU-SOmkWcVuk4ypiGqP7URnrjFjMk","-BLANK-", "x3ZhVWszU5vYU6wJJlg4RaJPKvc");
		//when(calendarService.query(any())).thenReturn(googleCalendarSbmSync);
		ContactItem contactItem = new ContactItem();
		contactItem.setEmail("testingdev@tma.com.vn");
		when(contactItemService.load(any())).thenReturn(contactItem);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(calendarService).put(any(GoogleCalendarSbmSync.class));
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
}
