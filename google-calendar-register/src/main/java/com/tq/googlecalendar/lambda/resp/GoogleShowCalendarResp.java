package com.tq.googlecalendar.lambda.resp;

import java.io.Serializable;

import com.tq.googlecalendar.resp.GoogleCalendarList;

public class GoogleShowCalendarResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8439099857438695107L;
	private GoogleCalendarList googleCalendarList;
	private String email;
	private String refreshToken;
	private String firstName;
	private String lastName;
	private String googleEmail;

	public GoogleCalendarList getGoogleCalendarList() {
		return googleCalendarList;
	}

	public void setGoogleCalendarList(GoogleCalendarList googleCalendarList) {
		this.googleCalendarList = googleCalendarList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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

	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}

	public GoogleShowCalendarResp() {
	}

	public GoogleShowCalendarResp(GoogleCalendarList googleCalendarList, String email, String refreshToken,
			String firstName, String lastName, String googleEmail) {
		this.googleCalendarList = googleCalendarList;
		this.email = email;
		this.refreshToken = refreshToken;
		this.firstName = firstName;
		this.lastName = lastName;
		this.googleEmail = googleEmail;
	}

	@Override
	public String toString() {
		return "GoogleShowCalendarResp [googleCalendarList=" + googleCalendarList + ", email=" + email
				+ ", refreshToken=" + refreshToken + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", googleEmail=" + googleEmail + "]";
	}

}
