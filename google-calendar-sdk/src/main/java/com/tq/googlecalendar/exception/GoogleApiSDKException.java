package com.tq.googlecalendar.exception;

public class GoogleApiSDKException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5449296246182002795L;

	public GoogleApiSDKException(String message) {
		super(message);
	}

	public GoogleApiSDKException(Throwable throwable) {
		super(throwable);
	}

	public GoogleApiSDKException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
