package com.tq.googlecalendar.lambda.resp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoogleConnectStatusResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7015678209285455380L;
	private boolean succeeded;
	private String status;
	private String googleEmail;
	private GoogleCalendarResponseBody responseBody;

	public boolean isSucceeded() {
		return succeeded;
	}

	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoogleEmail() {
		return googleEmail;
	}

	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}

	public GoogleCalendarResponseBody getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(GoogleCalendarResponseBody responseBody) {
		this.responseBody = responseBody;
	}

	public GoogleConnectStatusResponse(boolean succeeded, String status) {

		this.succeeded = succeeded;
		this.status = status;
	}

	public GoogleConnectStatusResponse(boolean succeeded, String status, GoogleCalendarResponseBody responseBody) {
		this.succeeded = succeeded;
		this.status = status;
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		return "GoogleConnectStatusResponse [succeeded=" + succeeded + ", status=" + status + ", googleEmail="
				+ googleEmail + ", responseBody=" + responseBody + "]";
	}

	public GoogleConnectStatusResponse() {

	}
}
