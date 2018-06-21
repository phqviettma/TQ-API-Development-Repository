package com.tq.cliniko.lambda.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patients implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5833332576959908466L;
	private List<PatientDetail> patients;

	public List<PatientDetail> getPatients() {
		return patients;
	}

	public void setPatients(List<PatientDetail> patients) {
		this.patients = patients;
	}

	public Patients(List<PatientDetail> patients) {
		this.patients = patients;
	}

	public Patients() {

	}

	@Override
	public String toString() {
		return "Patients [patients=" + patients + "]";
	}

}
