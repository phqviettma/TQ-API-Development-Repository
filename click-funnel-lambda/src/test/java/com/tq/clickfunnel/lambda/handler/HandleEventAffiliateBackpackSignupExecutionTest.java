package com.tq.clickfunnel.lambda.handler;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.context.CFLambdaContextImpl;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.MockEnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;
import com.tq.common.lambda.services.ISExternalServiceImpl;
import com.tq.common.lambda.services.RepositoryServiceImpl;
import com.tq.common.lambda.services.SBMExternalServiceImpl;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.JsonUtils;

public class HandleEventAffiliateBackpackSignupExecutionTest {
    private InterceptorEventPayloadProxy m_interceptorEvent;

    private LambdaContext m_lambdaContext;
   
    @Before
    public void init() throws IOException {
        MockEnvVar envVar = new MockEnvVar();
        Map<String, String> env = new HashMap<>();
        //InfusionSoft configuration
        env.put(Config.INFUSIONSOFT_API_NAME, "https://uf238.infusionsoft.com/api/xmlrpc");
        env.put(Config.INFUSIONSOFT_API_KEY, "");
        env.put(Config.INFUSIONSOFT_CLICKFUNNEL_AFFILIALTE_BACKPACK_SIGNUP_TAG, "300");
        envVar.setValueSystems(env);
        
        AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
        m_lambdaContext = LambdaContextImpl.builder()
                .withClient(client)
                .withEnvVar(envVar)
                .withiSExternalService(new ISExternalServiceImpl())
                .withRepositoryService(new RepositoryServiceImpl(client))
                .withSbmExternalService(new SBMExternalServiceImpl())
                .build();
        
        CFLambdaContext cfLambdaContext = new CFLambdaContextImpl(mock(Context.class), m_lambdaContext);
        
        m_interceptorEvent = new InterceptorEventPayloadProxy(m_lambdaContext) {
            @Override
            protected CFLambdaContext initialize(Context context, LambdaContext lambdaContext) {
                return cfLambdaContext;
            }
        };
    }
    
    @Test
    public void test() throws Exception {
        Context context = mock(Context.class);
        AwsProxyRequest req = new AwsProxyRequest();
        String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("contact-affilicate-backpack-singup.json"));
        req.setBody(jsonString);
        HashMap<String, String> event = new HashMap<>();
        event.put(EventType.EVENT_PARAMETER_NAME, EventType.AFFILICATE_SIGNUP);
        req.setQueryStringParameters(event);
        m_interceptorEvent.handleRequest(req, context);
    }
}
