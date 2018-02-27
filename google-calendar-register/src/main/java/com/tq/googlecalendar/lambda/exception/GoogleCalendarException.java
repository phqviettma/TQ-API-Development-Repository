package com.tq.googlecalendar.lambda.exception;

import com.tq.common.lambda.exception.TrueQuitException;

public class GoogleCalendarException extends TrueQuitException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9031755974945157850L;

	public GoogleCalendarException(String message) {
		super(message);
	}

	public GoogleCalendarException(Throwable throwable) {
		super(throwable);
	}

	public GoogleCalendarException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
