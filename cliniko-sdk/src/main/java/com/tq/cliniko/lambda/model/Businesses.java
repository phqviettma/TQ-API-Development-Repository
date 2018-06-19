package com.tq.cliniko.lambda.model;

import java.io.Serializable;

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

	@Override
	public String toString() {
		return "Businesses [id=" + id + ", business_name=" + business_name + ", country=" + country + ", practitioners="
				+ practitioners + "]";
	}

}
