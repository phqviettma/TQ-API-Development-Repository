package com.tq.clinikosbmsync.lambda.handler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.clinikosbmsync.lamdbda.context.Env;
import com.tq.common.lambda.dynamodb.model.ClinikoSbmSync;
import com.tq.common.lambda.dynamodb.model.ClinikoSyncStatus;
import com.tq.common.lambda.dynamodb.model.SbmCliniko;
import com.tq.common.lambda.dynamodb.service.ClinikoItemService;
import com.tq.common.lambda.dynamodb.service.ClinikoSyncToSbmService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.simplybook.impl.SbmBreakTimeManagement;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.SbmUnitService;
import com.tq.simplybook.service.SpecialdayServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

public class ClinikoSyncHandlerTest {
	private Env m_env = MockUtil.mockEnv();
	private static final String API_KEY = "";
	private Context m_context = mock(Context.class);
	private AmazonDynamoDB amazonDynamoDB = mock(AmazonDynamoDB.class);
	private SpecialdayServiceSbm specialdayService = mock(SpecialdayServiceSbm.class);
	private TokenServiceSbm tokenService = mock(TokenServiceSbm.class);
	private SbmBreakTimeManagement sbmTimeManagement = new SbmBreakTimeManagement();
	private ClinikoSyncToSbmService clinikoSyncService = mock(ClinikoSyncToSbmService.class);
	private ClinikoItemService clinikoItemService = mock(ClinikoItemService.class);
	private SbmClinikoSyncService sbmClinikoSyncService = mock(SbmClinikoSyncService.class);
	private SbmUnitService unitService = mock(SbmUnitService.class);
	private BookingServiceSbm bookingService = mock(BookingServiceSbm.class);
	ClinikoSyncHandler handler = new ClinikoSyncHandler(m_env, amazonDynamoDB, specialdayService, tokenService,
			sbmTimeManagement, clinikoSyncService, clinikoItemService, sbmClinikoSyncService, unitService,
			bookingService);

	@Test
	public void testSyncHandler() {
		String jsonString = JsonUtils
				.getJsonString(this.getClass().getClassLoader().getResourceAsStream("cliniko_appointment.json"));
		AwsProxyRequest req = new AwsProxyRequest();
		req.setBody(jsonString);
		ClinikoSbmSync clinikoSbmSync = new ClinikoSbmSync(API_KEY, "suongpham53@gmail.com", "62052-95259", "1-5");
		when(clinikoSyncService.queryWithIndex(any())).thenReturn(clinikoSbmSync);
		List<ClinikoSyncStatus> clinikoSync = new ArrayList<>();
		clinikoSync.add(new ClinikoSyncStatus(ClinikoSyncStatus.CHECK_KEY, API_KEY, 190005555L, null));
		when(clinikoItemService.queryIndex()).thenReturn(clinikoSync);
		SbmCliniko sbmCliniko = new SbmCliniko(1186181382203654900L, 106383666L, 1, API_KEY, "cliniko");
		when(sbmClinikoSyncService.queryIndex(any())).thenReturn(sbmCliniko);
		AwsProxyResponse response = handler.handleRequest(req, m_context);
		assertEquals(200, response.getStatusCode());
	}
}
