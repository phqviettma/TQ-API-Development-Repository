package com.tq.common.lambda.exception;

public class TrueQuitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3773859558050280711L;
	
	public TrueQuitException(String message) {
		super(message);
	}

	public TrueQuitException(Throwable throwable) {
		super(throwable);
	}

	public TrueQuitException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
