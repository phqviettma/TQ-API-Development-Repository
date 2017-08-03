package com.tq.clickfunnel.lambda.exception;

public class CFLambdaException  extends RuntimeException {
    
    private static final long serialVersionUID = -5130452374272341534L;

    public CFLambdaException(String message) {
        super(message);
    }

    public CFLambdaException(Throwable throwable) {
        super(throwable);
    }

    public CFLambdaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
