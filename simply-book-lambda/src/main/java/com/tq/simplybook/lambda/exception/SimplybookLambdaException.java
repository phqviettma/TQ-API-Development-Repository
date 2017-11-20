package com.tq.simplybook.lambda.exception;

public class SimplybookLambdaException extends Exception {

    private static final long serialVersionUID = 1695872757341111677L;

    public SimplybookLambdaException(String message) {
        super(message);
    }

    public SimplybookLambdaException(Throwable throwable) {
        super(throwable);
    }

    public SimplybookLambdaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
