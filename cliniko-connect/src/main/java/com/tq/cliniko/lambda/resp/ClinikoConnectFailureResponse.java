package com.tq.cliniko.lambda.resp;

import java.io.Serializable;

public class ClinikoConnectFailureResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -292184646174034734L;
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

	@Override
	public String toString() {
		return "ClinikoConnectFailureResponse [succeeded=" + succeeded + ", errorMessage=" + errorMessage
				+ ", statusCode=" + statusCode + "]";
	}

	public ClinikoConnectFailureResponse(boolean succeeded, String errorMessage, int statusCode) {

		this.succeeded = succeeded;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public ClinikoConnectFailureResponse() {

	}

}
