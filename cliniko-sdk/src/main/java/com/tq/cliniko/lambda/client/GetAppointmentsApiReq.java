package com.tq.cliniko.lambda.client;

public class GetAppointmentsApiReq extends QueryClinikoApiReq {
	public GetAppointmentsApiReq(String baseApiUrl, String apiKey, String queryStatement) {
		super(baseApiUrl, apiKey, "GET", "appointments", null, queryStatement, null);
	}
}
