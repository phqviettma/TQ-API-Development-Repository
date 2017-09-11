package com.tq.cliniko.lambda.client;

public class GetAppointmentsApiReq extends QueryClinikoApiReq {
	public GetAppointmentsApiReq(Env env, String queryStatement) {
		super(env.getApiUrl(),env.getApiKey(), "GET", "appointments", null, queryStatement, null);
	}
}
