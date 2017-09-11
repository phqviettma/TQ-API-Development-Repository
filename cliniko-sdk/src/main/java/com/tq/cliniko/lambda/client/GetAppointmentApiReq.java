package com.tq.cliniko.lambda.client;

public class GetAppointmentApiReq extends QueryClinikoApiReq{

	public GetAppointmentApiReq(Env env, String id) {
		super(env.getApiUrl(), env.getApiKey(), "GET", "appointments" + "/" + id, null, null, null);
	
	}

}
