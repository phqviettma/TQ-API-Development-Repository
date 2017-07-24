package com.tq.clickfunnel.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.service.ContactServiceInf;

public class CreateContactHandler implements RequestHandler<AwsProxyRequest,  AwsProxyResponse> {
    
    private static final Logger log = LoggerFactory.getLogger(CreateContactHandler.class);
    public ContactServiceInf contactServiceInf = new ContactServiceImpl();

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        log.info("{} ", input.getBody());
        AwsProxyResponse resp = new  AwsProxyResponse();
        return null;
    }
}
