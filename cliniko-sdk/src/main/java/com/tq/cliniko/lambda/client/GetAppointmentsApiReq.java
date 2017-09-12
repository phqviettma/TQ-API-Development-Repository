package com.tq.cliniko.lambda.client;

public class GetAppointmentsApiReq extends QueryClinikoApiReq {
	public GetAppointmentsApiReq(String apiKey, String queryStatement) {
		super(apiKey, "GET", "appointments", queryStatement);
	}
}
