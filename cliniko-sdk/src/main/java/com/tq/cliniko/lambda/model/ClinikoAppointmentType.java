package com.tq.cliniko.lambda.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClinikoAppointmentType {
	private List<AppointmentType> appointment_types;

	public List<AppointmentType> getAppointment_types() {
		return appointment_types;
	}

	public void setAppointment_types(List<AppointmentType> appointment_types) {
		this.appointment_types = appointment_types;
	}

	@Override
	public String toString() {
		return "ClinikoAppointmentType [appointment_types=" + appointment_types + "]";
	}

}
