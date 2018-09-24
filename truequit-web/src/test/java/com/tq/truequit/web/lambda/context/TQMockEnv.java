package com.tq.truequit.web.lambda.context;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tq.truequit.lambda.context.ShowBookingLambdaEnv;

public class TQMockEnv {
	public static ShowBookingLambdaEnv mockEnv() {
		ShowBookingLambdaEnv env = mock(ShowBookingLambdaEnv.class);
		when(env.getInfusionSoftApiName()).thenReturn("https://fd940.infusionsoft.com/api/xmlrpc");
		when(env.getInfusionSoftApiKey()).thenReturn("22af7f8620fe2cf4560b9fe01f7b8969");
		when(env.getInfusionSoftAppliedNoShowTag()).thenReturn(911);
		return env;
	}
}
