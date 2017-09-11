package com.tq.cliniko.lambda.client;

public class CreateAppointmentApiReq extends CreateClinikoApiReq {

	public CreateAppointmentApiReq(Env env, Object object) {
		super(env.getApiUrl(), env.getApiKey(), "appointments", object);
	}

	
	
}
