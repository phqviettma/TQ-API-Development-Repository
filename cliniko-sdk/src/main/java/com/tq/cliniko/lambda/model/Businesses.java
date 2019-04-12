package com.tq.cliniko.lambda.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Businesses implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5115373579227542273L;
	private Integer id;
	private String business_name;
	private String country;
	private Links practitioners;
	private List<Integer> appointment_type_ids;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Links getPractitioners() {
		return practitioners;
	}

	public void setPractitioners(Links practitioners) {
		this.practitioners = practitioners;
	}

	public Businesses withPractitioner(Links practitioner) {
		this.setPractitioners(practitioner);
		return this;
	}

	public List<Integer> getAppointment_type_ids() {
		return appointment_type_ids;
	}

	public void setAppointment_type_ids(List<Integer> appointment_type_ids) {
		this.appointment_type_ids = appointment_type_ids;
	}

	@Override
	public String toString() {
		return "Businesses [id=" + id + ", business_name=" + business_name + ", country=" + country + ", practitioners="
				+ practitioners + ", appointment_type_ids=" + appointment_type_ids + "]";
	}
}
