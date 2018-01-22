package com.tq.calendar.lambda.model;

import java.io.Serializable;

public class UserInfoResp implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 4213124658354140623L;
	private String name;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String accessToken;
	private String refreshToken;
	private String googleEmail;
	

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
	

	@Override
	public String toString() {
		return "UserInfoResp [name=" + name + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", gender=" + gender + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken
				+ ", googleEmail=" + googleEmail + "]";
	}

}
