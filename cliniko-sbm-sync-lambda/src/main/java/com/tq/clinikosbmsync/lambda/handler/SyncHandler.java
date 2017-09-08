package com.tq.clinikosbmsync.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SyncHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger m_log = LoggerFactory.getLogger(SyncHandler.class);
    
    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        return null;
    }
}
