package com.tq.clickfunnel.lambda.handler;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;

public class HandleNotFoundEventExecution extends AbstractEventPayloadExecution {
    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        String event = input.getQueryStringParameters().get(EventType.EVENT_PARAMETER_NAME);
        throw new CFLambdaException("Not found " + event + " .");
    }
}
