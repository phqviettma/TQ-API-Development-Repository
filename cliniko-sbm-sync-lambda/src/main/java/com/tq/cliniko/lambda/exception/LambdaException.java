package com.tq.cliniko.lambda.exception;

public class LambdaException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1371205911631164853L;

	public LambdaException(String message) {
		super(message);
	}

	public LambdaException(Throwable throwable) {
		super(throwable);
	}

	public LambdaException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
