package com.tq.calendar.exception;

public class GCalendarSDKException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5449296246182002795L;

	public GCalendarSDKException(String message) {
		super(message);
	}

	public GCalendarSDKException(Throwable throwable) {
		super(throwable);
	}

	public GCalendarSDKException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
