package com.tq.cliniko.lambda.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentsInfo {
	private List<AppointmentInfo> appointments;


	public List<AppointmentInfo> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentInfo> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String toString() {
		return "AppointmentsInfo [appointments=" + appointments + "]";
	}
	
	
}
