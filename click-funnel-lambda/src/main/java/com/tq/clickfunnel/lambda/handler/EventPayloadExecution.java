package com.tq.clickfunnel.lambda.handler;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.exception.ClickFunnelLambdaException;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;

public interface EventPayloadExecution {
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext proxyContext) throws ClickFunnelLambdaException;
}
