package com.tq.cliniko.lambda.model;

public class PatientPhoneNumber {
	private String number;
	private String phone_type;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhone_type() {
		return phone_type;
	}

	public void setPhone_type(String phone_type) {
		this.phone_type = phone_type;
	}
	public PatientPhoneNumber() {
	}

	public PatientPhoneNumber(String number) {
		this.number = number;
		this.phone_type = "Mobile";
	}

	@Override
	public String toString() {
		return "PatientPhoneNumber [number=" + number + ", phone_type=" + phone_type + "]";
	}

}