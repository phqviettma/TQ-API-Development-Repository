package com.tq.clickfunnel.lambda.handler;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;

public abstract class AbstractEventPayloadExecution implements EventPayloadExecution {
    private static final Logger log = Logger.getLogger(AbstractEventPayloadExecution.class);
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
         } catch (Exception e) {
             log.error("", e);
             String rebuild = String.format("{\"error\": \"%s\"}", e.getMessage());
             resp.setBody(rebuild);
             resp.setHeaders(input.getHeaders());
             resp.setStatusCode(200);
             return resp;
         }
        return resp;
    }
    public abstract AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException;
}
