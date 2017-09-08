package com.tq.cliniko.lambda.client;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestClinikoClient {
	
	@Test
	public void testGet() throws Exception {
		Env env = mock(Env.class);
		when(env.getApiKey()).thenReturn("b66b4caab8e4fd3e03b1dc88aa064339");
		when(env.getApiUrl()).thenReturn("https://api.cliniko.com/v1");
		String response = ClinikoClient.request(new GetAppointmentsApiReq(env, null));
		System.out.println(response);
		response = ClinikoClient.request(new GetAppointmentsApiReq(env, "appointment_start:>2017-08-25T07:47:04Z"));
		System.out.println(response);
	}
}
