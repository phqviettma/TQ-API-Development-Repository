package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.model.LatestClinikoAppointment;
import com.tq.common.lambda.dynamodb.service.LatestClinikoAppointmentService;

public class LatestClinikoAppointmentWrapper {
	private LatestClinikoAppointmentService service;

	public LatestClinikoAppointmentWrapper(LatestClinikoAppointmentService service) {
		this.service = service;
	}

	public LatestClinikoAppointment load() { 
		return service.load(LatestClinikoAppointment.LATEST_UPDATED_KEY);
	}

	public void put(LatestClinikoAppointment latestClinikoAppointment) {
		service.put(latestClinikoAppointment);
	}

	public void delete(LatestClinikoAppointment latestClinikoAppointment) {
		service.delete(latestClinikoAppointment);
	}
}
