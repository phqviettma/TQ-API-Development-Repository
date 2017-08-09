package com.tq.clickfunnel.lambda.context;

import com.tq.common.lambda.context.LambdaContext;

public interface CFLambdaContext {
    public com.amazonaws.services.lambda.runtime.Context getAwsProxyContext();
    
    public void wrapAwsProxyContext(com.amazonaws.services.lambda.runtime.Context context);
    
    public LambdaContext getLambdaContext();
}
