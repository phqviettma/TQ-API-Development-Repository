package com.tq.clickfunnel.lambda.handler;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;

public class HandleEventUpdatedOrderExecution extends HandleEventOrderExecution {

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload contactPayLoad, CFLambdaContext cfLambdaContext)
            throws CFLambdaException {
        throw new CFLambdaException("Not support for the updated order so far.");
    }

}
