package com.tq.cliniko.lambda.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6813434673815772232L;
	private String name;
	private String country;
	private String time_zone;
	private String country_code;
	private String email_from;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getEmail_from() {
		return email_from;
	}

	public void setEmail_from(String email_from) {
		this.email_from = email_from;
	}

	public Account withTimeZone(String timeZone) {
		this.setTime_zone(timeZone);
		return this;
	}

	public Account withCountry(String country) {
		this.setCountry(country);
		return this;
	}

	public Account(String name, String country, String timezone, String country_code, String email_from) {

		this.name = name;
		this.country = country;
		this.time_zone = timezone;
		this.country_code = country_code;
		this.email_from = email_from;
	}

	@Override
	public String toString() {
		return "Account [name=" + name + ", country=" + country + ", time_zone=" + time_zone + ", country_code="
				+ country_code + ", email_from=" + email_from + "]";
	}

	public Account() {
		super();
	}

}
