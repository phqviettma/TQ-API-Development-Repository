package com.tq.gcsyncsbm.lambda.model;

public class GeneralAppt {
	private String appointmentStart;
	private String appointmentEnd;

	public String getAppointmentStart() {
		return appointmentStart;
	}

	public void setAppointmentStart(String appointmentStart) {
		this.appointmentStart = appointmentStart;
	}

	public String getAppointmentEnd() {
		return appointmentEnd;
	}

	public void setAppointmentEnd(String appointmentEnd) {
		this.appointmentEnd = appointmentEnd;
	}

	public GeneralAppt(String appointmentStart, String appointmentEnd) {

		this.appointmentStart = appointmentStart;
		this.appointmentEnd = appointmentEnd;
	}

	public GeneralAppt() {

	}

	@Override
	public String toString() {
		return "GeneralAppt [appointmentStart=" + appointmentStart + ", appointmentEnd=" + appointmentEnd + "]";
	}
	

}
