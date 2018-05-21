package com.tq.googlecalendar.lambda.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleRegisterParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 815473435599519161L;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String accessToken;
	private String refreshToken;
	private String googleEmail;
	private List<String> googleCalendarId;

	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public List<String> getGoogleCalendarId() {
		return googleCalendarId;
	}

	public void setGoogleCalendarId(List<String> googleCalendarId) {
		this.googleCalendarId = googleCalendarId;
	}

	public GoogleRegisterParams(String firstName, String lastName, String email, String refreshToken,
			String googleEmail) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;

		this.refreshToken = refreshToken;
		this.googleEmail = googleEmail;
	}

	public GoogleRegisterParams(String firstName, String lastName, String email, String refreshToken,
			String googleEmail, List<String> googleCalendarId) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.refreshToken = refreshToken;
		this.googleEmail = googleEmail;
		this.googleCalendarId = googleCalendarId;
	}

	public GoogleRegisterParams() {

	}

	@Override
	public String toString() {
		return "GoogleRegisterParams [name=" + name + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", gender=" + gender + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken
				+ ", googleEmail=" + googleEmail + "]";
	}

}
