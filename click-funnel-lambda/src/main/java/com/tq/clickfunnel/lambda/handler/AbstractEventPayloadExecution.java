package com.tq.clickfunnel.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;

public abstract class AbstractEventPayloadExecution implements EventPayloadExecution {
    private static final Logger log = LoggerFactory.getLogger(AbstractEventPayloadExecution.class);
    protected ObjectMapper m_mapper = new ObjectMapper();
    
    public <T> void handleResponse(AwsProxyRequest input, AwsProxyResponse resp, T item) {
        String rebuild = null;
        try {
            rebuild = item == null ? String.format("{\"item\": \"%s\"}", "null") : m_mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("", e);
            rebuild = String.format("{\"error\": \"%s\"}", e.getMessage());
            // ignore
        }
        resp.setBody(rebuild);
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(200);
    }
    
    @Override
    public AwsProxyResponse execute(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
         try {
             resp = handleLambdaProxy(input, cfLambdaContext);
         } catch (CFLambdaException cfLambdaException) {
             log.error("", cfLambdaException);
             String rebuild = String.format("{\"error\": \"%s\"}", cfLambdaException.getMessage());
             resp.setBody(rebuild);
             resp.setHeaders(input.getHeaders());
             resp.setStatusCode(200);
             return resp;
         }
        return resp;
    }
    public abstract AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException;
}
