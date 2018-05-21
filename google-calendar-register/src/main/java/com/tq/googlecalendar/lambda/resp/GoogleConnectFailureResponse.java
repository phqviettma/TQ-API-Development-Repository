package com.tq.googlecalendar.lambda.resp;

import java.io.Serializable;

public class GoogleConnectFailureResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7954775776200207873L;
	private boolean succeeded;
	private String errorMessage;
	private int statusCode;

	public boolean isSucceeded() {
		return succeeded;
	}

	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public GoogleConnectFailureResponse(boolean succeeded, String errorMessage, int statusCode) {

		this.succeeded = succeeded;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public GoogleConnectFailureResponse() {

	}

	@Override
	public String toString() {
		return "GoogleConnectFailureResponse [succeeded=" + succeeded + ", errorMessage=" + errorMessage
				+ ", statusCode=" + statusCode + "]";
	}

}
