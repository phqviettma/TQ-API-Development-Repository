package com.tq.clickfunnel.lambda.handler;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.exception.ClickFunnelLambdaException;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;

public class HandleNotFoundEventExecution extends AbstractEventPayloadExecution {

    @Override
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext proxyContext) throws ClickFunnelLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        String event = input.getQueryStringParameters().get(EventType.EVENT_PARAMETER_NAME);
        String rebuild = String.format("{\"error\": \"%s\"}", "Not found " + event + " .");
        resp.setBody(rebuild);
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(503);
        return resp;
    }

}
