package com.tq.simplybook.exception;

public class SbmDateTimeException extends Exception {

    private static final long serialVersionUID = 6176873883537973799L;

    public SbmDateTimeException(String message) {
        super(message);
    }

    public SbmDateTimeException(Throwable throwable) {
        super(throwable);
    }

    public SbmDateTimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
