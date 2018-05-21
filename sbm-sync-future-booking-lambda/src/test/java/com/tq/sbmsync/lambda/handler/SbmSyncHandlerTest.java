package com.tq.sbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.GoogleCalendarSbmSync;
import com.tq.common.lambda.dynamodb.service.ClinikoCompanyInfoService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.GoogleCalendarDbService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.dynamodb.service.SbmGoogleCalendarDbService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.googlecalendar.impl.TokenGoogleCalendarImpl;
import com.tq.googlecalendar.service.TokenGoogleCalendarService;
import com.tq.simplybook.context.Env;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.service.TokenServiceSbm;

public class SbmSyncHandlerTest {
	private Env env = MockUtil.mockEnv();
	private TokenServiceSbm tokenService = new TokenServiceImpl();
	private SbmClinikoSyncService sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private GoogleCalendarDbService googleCalendarDbService = mock(GoogleCalendarDbService.class);
	private TokenGoogleCalendarService tokenCalendarService = new TokenGoogleCalendarImpl();
	private SbmGoogleCalendarDbService sbmGoogleCalendarService = mock(SbmGoogleCalendarDbService.class);
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);
	private BookingServiceSbmImpl bookingSbmService = new BookingServiceSbmImpl();
	private ClinikoCompanyInfoService clinikoCompanyService = mock(ClinikoCompanyInfoService.class);
	private SbmSyncClinikoHandler clinikoHandler = new SbmSyncClinikoHandler(clinikoSyncService, bookingSbmService,
			tokenService, env, sbmClinikoSyncService, clinikoCompanyService);
	private SbmSyncGCHandler gcHandler = new SbmSyncGCHandler(googleCalendarDbService, env, bookingSbmService,
			tokenService, tokenCalendarService, sbmGoogleCalendarService);
	private SbmSyncHandler sbmSyncHandler = new SbmSyncHandler(env, clinikoSyncService, tokenService,
			sbmClinikoSyncService, googleCalendarDbService, tokenCalendarService,
			sbmGoogleCalendarService, clinikoHandler, gcHandler);

	@Test
	public void testHandlerTest() {
		AwsProxyRequest req = new AwsProxyRequest();
		Context context = mock(Context.class);
		String body = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("sbm_sync_resource.json"));
		req.setBody(body);
		List<GoogleCalendarSbmSync> googleCalendarSbmSync = Arrays.asList(new GoogleCalendarSbmSync());
		when(googleCalendarDbService.queryEmail(any())).thenReturn(googleCalendarSbmSync);
		ClinikoSbmSync clinikoSbm = new ClinikoSbmSync("ff997f7d491b555f227262870a2717c1", "suongpham53@gmail.com",
				"58837-89589", "1-6");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbm);
		AwsProxyResponse response = sbmSyncHandler.handleRequest(req, context);
		assertEquals(200, response.getStatusCode());
	}
}
