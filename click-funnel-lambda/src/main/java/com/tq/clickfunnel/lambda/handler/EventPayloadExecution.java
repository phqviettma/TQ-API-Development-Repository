package com.tq.clickfunnel.lambda.handler;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;

public interface EventPayloadExecution {
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException;
}
