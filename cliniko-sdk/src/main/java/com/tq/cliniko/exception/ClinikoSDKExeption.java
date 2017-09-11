package com.tq.cliniko.exception;

public class ClinikoSDKExeption extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1533296112108629168L;

	public ClinikoSDKExeption(String message) {
		super(message);
	}

	public ClinikoSDKExeption(Throwable throwable) {
		super(throwable);
	}

	public ClinikoSDKExeption(String message, Throwable throwable) {
		super(message, throwable);
	}

}
