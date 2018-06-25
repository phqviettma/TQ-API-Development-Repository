package com.tq.cliniko.lambda.model;

import java.io.Serializable;
import java.util.List;

public class PatientPostReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7217843934473261644L;
	private String first_name;
	private String last_name;
	private String email;
	private List<PatientPhoneNumber> patient_phone_numbers;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<PatientPhoneNumber> getPatient_phone_numbers() {
		return patient_phone_numbers;
	}

	public void setPatient_phone_numbers(List<PatientPhoneNumber> patient_phone_numbers) {
		this.patient_phone_numbers = patient_phone_numbers;
	}

	@Override
	public String toString() {
		return "PatientPostReq [first_name=" + first_name + ", last_name=" + last_name + ", email=" + email
				+ ", patient_phone_numbers=" + patient_phone_numbers + "]";
	}

	public PatientPostReq(String first_name, String last_name, String email,
			List<PatientPhoneNumber> patientPhoneNumber) {

		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.patient_phone_numbers = patientPhoneNumber;
	}

	public PatientPostReq() {

	}

	

}
