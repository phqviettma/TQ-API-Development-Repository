package com.tq.cliniko.lambda.req;

import java.io.Serializable;

public class ClinikoBodyRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1846363720055732462L;
	private String appointment_start;
	private String appointment_end;

	public String getAppointment_start() {
		return appointment_start;
	}

	public void setAppointment_start(String appointment_start) {
		this.appointment_start = appointment_start;
	}

	public String getAppointment_end() {
		return appointment_end;
	}

	public void setAppointment_end(String appointment_end) {
		this.appointment_end = appointment_end;
	}

	public ClinikoBodyRequest(String appointment_start, String appointment_end) {

		this.appointment_start = appointment_start;
		this.appointment_end = appointment_end;
	}

	public ClinikoBodyRequest() {

	}

	@Override
	public String toString() {
		return "ClinikoBodyRequest [appointment_start=" + appointment_start + ", appointment_end=" + appointment_end
				+ "]";
	}

}
