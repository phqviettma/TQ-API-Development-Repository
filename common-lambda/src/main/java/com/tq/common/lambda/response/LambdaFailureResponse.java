package com.tq.common.lambda.response;

import java.io.Serializable;

public class LambdaFailureResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public LambdaFailureResponse(boolean succeeded, String errorMessage, int statusCode) {

		this.succeeded = succeeded;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}

	public LambdaFailureResponse() {

	}

	@Override
	public String toString() {
		return "LambdaFailureResponse [succeeded=" + succeeded + ", errorMessage=" + errorMessage
				+ ", statusCode=" + statusCode + "]";
	}
}
