package com.tq.cliniko.lambda.client;

public class CreateAppointmentApiReq extends CreateClinikoApiReq {

	public CreateAppointmentApiReq(String baseApiUrl, String apiKey, Object object) {
		super(baseApiUrl, apiKey, "appointments", object);
	}
}
