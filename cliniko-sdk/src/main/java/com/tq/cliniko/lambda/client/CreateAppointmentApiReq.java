package com.tq.cliniko.lambda.client;

public class CreateAppointmentApiReq extends PostClinikoApiReq {
	public CreateAppointmentApiReq(String apiKey, Object object) {
		super(apiKey, "appointments", object);
	}
}
