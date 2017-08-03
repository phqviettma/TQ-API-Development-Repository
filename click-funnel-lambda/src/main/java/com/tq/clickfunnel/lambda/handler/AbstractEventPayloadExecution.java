package com.tq.clickfunnel.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractEventPayloadExecution implements EventPayloadExecution {
    private static final Logger log = LoggerFactory.getLogger(AbstractEventPayloadExecution.class);
    protected ObjectMapper m_mapper = new ObjectMapper();
    
    public <T> void handleResponse(AwsProxyRequest input, AwsProxyResponse resp, T item) {
        String rebuild = null;
        try {
            rebuild = item == null ? String.format("{\"contact\": \"%s\"}", "null") : m_mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            log.error("", e);
            // ignore
        }
        resp.setBody(rebuild);
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(200);
    }
}
