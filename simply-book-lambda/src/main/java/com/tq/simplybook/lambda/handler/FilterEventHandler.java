package com.tq.simplybook.lambda.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.service.DataServiceInf;
import com.tq.simplybook.lambda.model.PayloadCallback;

/**
 * http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-interfaces.html
 */
public class FilterEventHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger log = LoggerFactory.getLogger(FilterEventHandler.class);
    private ObjectMapper m_jsonMapper = new ObjectMapper();
    public DataServiceInf dataServiceInf = new DataServiceImpl();

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        AwsProxyResponse res = new AwsProxyResponse();
        PayloadCallback payLoad = getPayloadCallback(input.getBody());
        if (payLoad != null) {
            if ("book".equalsIgnoreCase(payLoad.getNotification_type()) || "cancel".equalsIgnoreCase(payLoad.getNotification_type())) {
                // do something
            }
        }
        return res;
    }

    public PayloadCallback getPayloadCallback(String value) {
        PayloadCallback payLoad = null;
        try {
            payLoad = m_jsonMapper.readValue(value, PayloadCallback.class);
        } catch (IOException e) {
            log.error("Error during parsing {} : {} .", value ,  e);
        }
        return payLoad;
    }
}
