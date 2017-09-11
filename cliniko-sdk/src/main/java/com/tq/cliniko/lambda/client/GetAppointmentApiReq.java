package com.tq.cliniko.lambda.client;

public class GetAppointmentApiReq extends QueryClinikoApiReq{
	public GetAppointmentApiReq(String baseApiUrl, String apiKey, String id) {
		super(baseApiUrl, apiKey, "GET", "appointments" + "/" + id, null, null, null);
	}
}
