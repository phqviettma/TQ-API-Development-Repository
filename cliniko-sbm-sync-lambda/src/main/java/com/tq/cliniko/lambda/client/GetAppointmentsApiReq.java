package com.tq.cliniko.lambda.client;

public class GetAppointmentsApiReq extends QueryClinikoApiReq {
	public GetAppointmentsApiReq(Env env, String queyStatement) {
		super(env, "GET", "appointments", null, queyStatement, null);
	}
}
