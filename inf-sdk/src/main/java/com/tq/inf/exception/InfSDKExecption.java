package com.tq.inf.exception;

public class InfSDKExecption extends Exception {
    private static final long serialVersionUID = -4628600070819149655L;

    public InfSDKExecption(String message) {
        super(message);
    }

    public InfSDKExecption(Throwable throwable) {
        super(throwable);
    }

    public InfSDKExecption(String message, Throwable throwable) {
        super(message, throwable);
    }
}
