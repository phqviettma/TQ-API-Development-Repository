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
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.impl.ClinikiAppointmentServiceImpl;
import com.tq.cliniko.lambda.dynamodb.dao.LatestClinikoApptsDaoImpl;
import com.tq.cliniko.service.ClinikoAppointmentService;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;
import com.tq.common.lambda.dynamodb.dao.ContactItemDaoImpl;
import com.tq.common.lambda.dynamodb.dao.LatestClinikoApptsImpl;
import com.tq.common.lambda.dynamodb.dao.SbmClinikoSyncDaoImpl;
import com.tq.common.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceImpl;
import com.tq.common.lambda.dynamodb.impl.LatestClinikoApptServiceWrapper;
import com.tq.common.lambda.dynamodb.impl.SbmClinikoSyncImpl;
import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.LatestClinikoApptService;
import com.tq.common.lambda.dynamodb.service.SbmClinikoSyncService;
import com.tq.common.lambda.utils.DynamodbUtils;
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
    private static int STATUS_CODE = 200;
    
    private ObjectMapper m_jsonMapper = new ObjectMapper();
    public DataServiceInf dataServiceInf = new DataServiceImpl();
    private Env m_env = Env.load();
    private AmazonDynamoDB m_amazonDynamoDB = DynamodbUtils.getAmazonDynamoDB(m_env.getRegions(), m_env.getAwsAccessKeyId(),
            m_env.getAwsSecretAccessKey());
    private ContactServiceInf m_csi = new ContactServiceImpl();
    private BookingServiceSbm m_bss = new BookingServiceSbmImpl();
    private TokenServiceSbm m_tss = new TokenServiceImpl();
    private SbmClinikoSyncService m_scs = new SbmClinikoSyncImpl(new SbmClinikoSyncDaoImpl(m_amazonDynamoDB));
    private ContactItemService m_cis = new ContactItemServiceImpl(new ContactItemDaoImpl(m_amazonDynamoDB));
    private LatestClinikoApptService m_lcs = new LatestClinikoApptServiceImpl(new LatestClinikoApptsImpl(m_amazonDynamoDB));
    private LatestClinikoApptServiceWrapper m_lcsw = new LatestClinikoApptServiceWrapper(m_lcs);
    private SimplyBookClinikoMapping m_scm = new SimplyBookClinikoMapping(m_env);
    private ClinikoAppointmentService m_cas = new ClinikiAppointmentServiceImpl(m_env.getClinikoApiKey());
    private InternalHandler m_createHandler = new CreateInternalHandler(m_env, m_tss, m_bss, m_csi, m_cis,m_scm, m_scs, m_lcsw);
    private InternalHandler m_cancelHandler = new CancelInternalHandler(m_env, m_tss, m_bss, m_csi, m_cis, m_scs, m_lcsw, m_cas);

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        AwsProxyResponse resp = new AwsProxyResponse();
        
        m_log.info("Received one request with body " + input.getBody());
        
        PayloadCallback payLoad = getPayloadCallback(input.getBody());
        boolean ignored = true;
        
        Throwable error = null;
        
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

            } catch (SbmSDKException | ClinikoSDKExeption e) {
                m_log.error("Processed notification: " + payLoad.getNotification_type() + " for booking ID: " 
                                    + payLoad.getBooking_id() + " results in error: ", e);
                error = e;
            }
        } 
        
        handleResponse(input, resp, payLoad,  error);
        
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
    
    private void handleResponse(AwsProxyRequest input, AwsProxyResponse resp, PayloadCallback pc, Throwable t) {
        String body = null;
        if(pc != null) {
            if (t != null){
                body = "ERROR";
            } else {
                body = "OK: ";
            }
        } else {
            body = "Invalid request body";
        }
        
        resp.setBody(body);
        resp.setHeaders(input.getHeaders());
        resp.setStatusCode(STATUS_CODE);
    }
}
