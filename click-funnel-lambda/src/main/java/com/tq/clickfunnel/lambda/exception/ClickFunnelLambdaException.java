package com.tq.clickfunnel.lambda.exception;

public class ClickFunnelLambdaException  extends RuntimeException {
    
    private static final long serialVersionUID = -5130452374272341534L;

    public ClickFunnelLambdaException(String message) {
        super(message);
    }

    public ClickFunnelLambdaException(Throwable throwable) {
        super(throwable);
    }

    public ClickFunnelLambdaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
