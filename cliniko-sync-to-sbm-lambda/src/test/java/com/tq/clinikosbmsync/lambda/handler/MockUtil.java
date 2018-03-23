package com.tq.clinikosbmsync.lambda.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.amazonaws.regions.Regions;
import com.tq.clinikosbmsync.lamdbda.context.Env;


public class MockUtil {
	public static Env mockEnv() {
		Env env = mock(Env.class);
		when(env.getRegions()).thenReturn(Regions.US_EAST_1);
		when(env.getAwsSecretAccessKey()).thenReturn("");
		when(env.getAwsAccessKeyId()).thenReturn("");
		when(env.getSimplyBookCompanyLogin()).thenReturn("canhcanh");
		when(env.getSimplyBookUser()).thenReturn("admin");
		when(env.getSimplyBookPassword()).thenReturn("");
		when(env.getSimplyBookServiceUrlLogin()).thenReturn("https://user-api.simplybook.asia/login");
		when(env.getSimplyBookAdminServiceUrl()).thenReturn("https://user-api.simplybook.asia/admin/");
		when(env.getCliniko_start_time()).thenReturn("09:00");
		when(env.getCliniko_end_time()).thenReturn("18:00");
		return env;
	}
	


	
}
