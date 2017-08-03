package com.tq.clickfunnel.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;

public class HandleEventCreatedOrderExecution extends AbstractEventPayloadExecution{
    private static final Logger log = LoggerFactory.getLogger(HandleEventCreatedOrderExecution.class);
    @Override
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext proxyContext) {
        log.info("create order:" + input.getBody());
        log.info("{}" , input.getQueryStringParameters());
        return null;
    }
}
