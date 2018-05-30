package com.tq.cliniko.impl;

import com.tq.cliniko.service.ClinikoAppointmentService;

public class ClinikoApiServiceBuilder {
	public ClinikoAppointmentService build(String apiKey) {
		return new ClinikiAppointmentServiceImpl(apiKey);
	}
}
