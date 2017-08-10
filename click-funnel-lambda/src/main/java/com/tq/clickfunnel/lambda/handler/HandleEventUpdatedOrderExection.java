package com.tq.clickfunnel.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;

public class HandleEventUpdatedOrderExection extends HandleEventOrderExecution {
    private static final Logger log = LoggerFactory.getLogger(HandleEventUpdatedOrderExection.class);

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload contactPayLoad,
            CFLambdaContext cfLambdaContext) {
        return null;
    }

}
