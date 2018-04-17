package com.tq.cliniko.lambda.model;

import java.io.Serializable;

public class PatientPostReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7217843934473261644L;
	private String first_name;
	private String last_name;

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	@Override
	public String toString() {
		return "PatientPostReq [first_name=" + first_name + ", last_name=" + last_name + "]";
	}

	public PatientPostReq(String first_name, String last_name) {

		this.first_name = first_name;
		this.last_name = last_name;
	}

	public PatientPostReq() {

	}

}
