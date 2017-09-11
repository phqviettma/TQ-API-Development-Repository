package com.tq.cliniko.lambda.client;

public class DeleteAppointmentApiReq extends DeleteClinikoApiReq {

	public DeleteAppointmentApiReq(Env env, String resource) {
		super(env.getApiUrl(),env.getApiKey(), resource);
	}
	
}
