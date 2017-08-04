package com.tq.simplybook.lambda.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;
import com.tq.inf.impl.ContactServiceImpl;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.service.ContactServiceInf;
import com.tq.inf.service.DataServiceInf;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.impl.BookingServiceSbmImpl;
import com.tq.simplybook.impl.TokenServiceImpl;
import com.tq.simplybook.lambda.context.Env;
import com.tq.simplybook.lambda.model.PayloadCallback;
import com.tq.simplybook.service.BookingServiceSbm;
import com.tq.simplybook.service.TokenServiceSbm;

/**
 * http://docs.aws.amazon.com/lambda/latest/dg/java-handler-using-predefined-
 * interfaces.html
 */
public class FilterEventHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger m_log = LoggerFactory.getLogger(FilterEventHandler.class);
    private ObjectMapper m_jsonMapper = new ObjectMapper();
    public DataServiceInf dataServiceInf = new DataServiceImpl();
    private Env m_env = Env.load();
    private AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getAmazonDynamoDB(Config.DYNAMODB_DEFAULT_REGION, m_env.getAwsAccessKeyId(),
            m_env.getAwsSecretAccessKey());

    private ContactServiceInf m_csi = new ContactServiceImpl();
    private BookingServiceSbm m_bss = new BookingServiceSbmImpl();
    private TokenServiceSbm m_tss = new TokenServiceImpl();
    private ContactItemService m_cis = new ContactItemServiceImpl(m_amazonDynamoDB);

    private InternalHandler m_createHandler = new CreateInternalHandler(m_env, m_tss, m_bss, m_csi, m_cis);
    private InternalHandler m_cancelHandler = new CancelInternalHandler(m_env, m_tss, m_bss, m_csi, m_cis);

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        AwsProxyResponse resp = new AwsProxyResponse();
        
        m_log.info("Received one request with body " + input.getBody());
        m_log.info("Received one request with header " + input.getHeaders());
        m_log.info("Received one request with StageVariables " + input.getStageVariables());
        m_log.info("Received one request with rescource " + input.getResource());
        m_log.info("Received one request with queryString " + input.getQueryString());
        m_log.info("Received one request with queryStringParameters " + input.getQueryStringParameters());
        
        PayloadCallback payLoad = getPayloadCallback(input.getBody());
        boolean ignored = true;

        if (payLoad != null) {
            try {
                if ("create".equalsIgnoreCase(payLoad.getNotification_type())) {
                    m_createHandler.handle(payLoad);
                } else if ("cancel".equalsIgnoreCase(payLoad.getNotification_type())) {
                    m_cancelHandler.handle(payLoad);
                } else {
                    ignored = false;
                }

                if (ignored) {
                    m_log.info("Notification:" + payLoad.getNotification_type() + " for booking ID:" 
                                    + payLoad.getBooking_id() + " is handled without error");
                } else {
                    m_log.info("Notification:" + payLoad.getNotification_type() + " for booking ID:" 
                                    + payLoad.getBooking_id() + " is unhandled");
                }

            } catch (SbmSDKException e) {
                m_log.error("Processed notification: " + payLoad.getNotification_type() + " for booking ID: " 
                                    + payLoad.getBooking_id() + " results in error: ", e);
                String rebuild = String.format("{\"error\": \"%s\"}", e.getMessage());
                resp.setBody(rebuild);
                resp.setHeaders(input.getHeaders());
                resp.setStatusCode(503);
                return resp;
            }
        }
        
        handleResponse(input, resp);
        
        return resp;
    }
    
    public PayloadCallback getPayloadCallback(String value) {
        PayloadCallback payLoad = null;
        try {
            payLoad = m_jsonMapper.readValue(value, PayloadCallback.class);
        } catch (IOException e) {
            m_log.error("Error during parsing {} : {} .", value, e);
        }
        return payLoad;
    }
    
    private void handleResponse(AwsProxyRequest input, AwsProxyResponse resp) {
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(200);
    }
}
