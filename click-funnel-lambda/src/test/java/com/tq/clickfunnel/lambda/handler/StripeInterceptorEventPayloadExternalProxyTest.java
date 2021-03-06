package com.tq.clickfunnel.lambda.handler;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.context.CFLambdaContextImpl;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.MockEnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.services.ISExternalServiceImpl;
import com.tq.common.lambda.services.RepositoryServiceImpl;
import com.tq.common.lambda.services.SBMExternalServiceImpl;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.JsonUtils;

import junit.framework.Assert;

public class StripeInterceptorEventPayloadExternalProxyTest {
    private static final Logger log = Logger.getLogger(StripeInterceptorEventPayloadExternalProxyTest.class);
    
    private InterceptorEventPayloadProxy m_interceptorEvent;

    private LambdaContext m_lambdaContext;
    
    public ObjectMapper mapper = new ObjectMapper();
    
    private Context m_context = mock(Context.class);
    
    private static MockEnvVar envVar = new MockEnvVar();
    static {
        Map<String, String> env = new HashMap<>();
        //InfusionSoft configuration
        env.put(Config.INFUSIONSOFT_API_NAME, "https://bh321.infusionsoft.com/api/xmlrpc");
        env.put(Config.INFUSIONSOFT_API_KEY, "");
        env.put(Config.INFUSION_ORDER_PROMO_CODE, "SIMPLY_BOOK_SERVICE_URL");
        //SimplyBookMe Configuration
        env.put(Config.SIMPLY_BOOK_COMPANY_LOGIN, "canhcanh");
        env.put(Config.SIMPLY_BOOK_USER_NAME, "admin");
        env.put(Config.SIMPLY_BOOK_PASSWORD, "");
        env.put(Config.SIMPLY_BOOK_API_KEY, "");
        env.put(Config.SIMPLY_BOOK_DEFAULT_USER_PASSWORD, "admin");
        //AWS configuration
        env.put(Config.AMAZON_ACCESS_KEY, "");
        env.put(Config.AMAZON_SECRET_ACCESS_KEY, "");
        env.put(Config.DYNAMODB_AWS_REGION, "us-east-1");
        
        envVar.setValueSystems(env);
    }
    
    @Before
    public void init() throws IOException {
        // Initialize the external environment for testing locally
        AmazonDynamoDB client = DynamodbUtils.getAmazonDynamoDBInEnv(envVar);
        m_lambdaContext = LambdaContextImpl.builder()
                .withClient(client)
                .withEnvVar(envVar)
                .withiSExternalService(new ISExternalServiceImpl())
                .withRepositoryService(new RepositoryServiceImpl(client))
                .withSbmExternalService(new SBMExternalServiceImpl())
                .build()
                ;
        CFLambdaContext cfLambdaContext = new CFLambdaContextImpl(m_context, m_lambdaContext);
        m_interceptorEvent = new InterceptorEventPayloadProxy(m_lambdaContext) {
            @Override
            protected CFLambdaContext initialize(Context context, LambdaContext lambdaContext) {
                return cfLambdaContext;
            }
        };
    }
    
    //@Test
    public void testCreatedExternalContact() throws IOException {
        // Simulator for receiving Contact of the click Funnel payload
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contactpayload.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.COTACT_CREATED);
        req.setQueryStringParameters(event);
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, m_context);
        log.info(response.getBody());
        ContactItem contactItem = mapper.readValue(response.getBody(), ContactItem.class);
        Assert.assertNotNull(contactItem);
        Assert.assertTrue(contactItem.getClient().getContactId() > 0);
    }
    
    @Test
    public void testCreatedExternalStripeOrderIntegration() throws IOException {
        // Simulator for receiving Order of the click Funnel payload
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-stripe2.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
        req.setQueryStringParameters(event);
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, m_context);
        log.info(response.getBody());
    }
    
   // @Test
    public void testDeletedExternalStripeOrderIntegration() throws IOException {
        // Simulator for receiving Order of the click Funnel payload
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-stripe2.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_DELETED);
        req.setQueryStringParameters(event);
        AwsProxyResponse response = m_interceptorEvent.handleRequest(req, m_context);
        log.info(response.getBody());
    }
}
