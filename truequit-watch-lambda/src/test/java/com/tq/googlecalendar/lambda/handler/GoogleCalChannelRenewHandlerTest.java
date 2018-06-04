package com.tq.googlecalendar.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.GoogleRenewChannelInfo;
import com.tq.common.lambda.dynamodb.service.GoogleCalRenewService;
import com.tq.googlecalendar.context.Env;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.impl.GoogleCalendarApiServiceBuilder;
import com.tq.googlecalendar.resp.Error;
import com.tq.googlecalendar.resp.ErrorResp;
import com.tq.googlecalendar.resp.TokenResp;
import com.tq.googlecalendar.resp.WatchEventResp;
import com.tq.googlecalendar.service.GoogleCalendarApiService;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;

public class GoogleCalChannelRenewHandlerTest {
	private GoogleCalendarApiServiceBuilder apiServiceBuilder = mock(GoogleCalendarApiServiceBuilder.class);
	private TokenGoogleCalendarService tokenCalendarService = mock(TokenGoogleCalendarService.class);
	private Env env = MockUtil.mockEnv();
	private GoogleCalendarApiService googleCalendarApiService = mock(GoogleCalendarApiService.class);
	private GoogleCalRenewService googleWatchChannelDbService = mock(GoogleCalRenewService.class);
	private GoogleCalChannelRenewHandler handler = new GoogleCalChannelRenewHandler(apiServiceBuilder,
			tokenCalendarService, env, googleWatchChannelDbService);

	@Test
	public void testWatchHandler() throws GoogleApiSDKException {
		AwsProxyRequest input = new AwsProxyRequest();
		Context context = mock(Context.class);
		GoogleRenewChannelInfo channelInfo = new GoogleRenewChannelInfo();
		channelInfo.setChannelId("channel id");
		channelInfo.setResourceId(" resource id");
		when(googleWatchChannelDbService.load(any())).thenReturn(channelInfo);
		List<GoogleRenewChannelInfo> channelInfos = new ArrayList<>();
		channelInfos.add(channelInfo);
		when(googleWatchChannelDbService.queryCheckingTime(any())).thenReturn(channelInfos );
		TokenResp tokenResp = new TokenResp();
		tokenResp.setAccess_token("accesstoken");
		when(tokenCalendarService.getToken(any())).thenReturn(tokenResp);
		ErrorResp value = new ErrorResp();
		Error error = new Error();
		error.setCode(404);
		error.setMessage("Channel id not found for project id");
		value.setError(error);
		when(googleCalendarApiService.stopWatchEvent(any())).thenReturn(value);
		WatchEventResp watchResponse = new WatchEventResp();
		watchResponse.setExpiration(1520671572000L);
		when(googleCalendarApiService.watchEvent(any(), any())).thenReturn(watchResponse);
		when(apiServiceBuilder.build(anyString())).thenReturn(googleCalendarApiService);
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		}).when(googleWatchChannelDbService).put(any(GoogleRenewChannelInfo.class));
		AwsProxyResponse response = handler.handleRequest(input, context);
		assertEquals(200, response.getStatusCode());
	}
}
