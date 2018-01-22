package com.tq.common.lambda.exception;

public class TrueQuitBadRequest extends TrueQuitException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1465738471570525375L;

	public TrueQuitBadRequest(String message) {
		super(message);
	}

	public TrueQuitBadRequest(Throwable throwable) {
		super(throwable);
	}

	public TrueQuitBadRequest(String message, Throwable throwable) {
		super(message, throwable);
	}

}
