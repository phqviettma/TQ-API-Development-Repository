package com.tq.simplybook.exception;

public class SbmSDKException extends Exception{
    private static final long serialVersionUID = -4628600070819149655L;

    public SbmSDKException(String message) {
        super(message);
    }

    public SbmSDKException(Throwable throwable) {
        super(throwable);
    }

    public SbmSDKException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
