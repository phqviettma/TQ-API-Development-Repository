package com.tq.cliniko.lambda.client;

public class GetAppointmentApiReq extends QueryClinikoApiReq{
	public GetAppointmentApiReq(String apiKey, String id) {
		super(apiKey, "GET", "appointments" + "/" + id, null);
	}
}
