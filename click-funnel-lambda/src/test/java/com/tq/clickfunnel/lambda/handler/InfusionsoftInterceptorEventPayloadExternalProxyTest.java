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
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.MockEnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.services.ISExternalServiceImpl;
import com.tq.common.lambda.services.RepositoryServiceImpl;
import com.tq.common.lambda.services.SBMExternalServiceImpl;
import com.tq.common.lambda.services.ISExternalServiceImpl.ISExternalServiceBuilder;
import com.tq.common.lambda.utils.DynamodbUtils;
import com.tq.common.lambda.utils.JsonUtils;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.impl.RecurringOrderImpl;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.RecurringOrderInf;

import junit.framework.Assert;

public class InfusionsoftInterceptorEventPayloadExternalProxyTest {
	 private static final Logger log = Logger.getLogger(InfusionsoftInterceptorEventPayloadExternalProxyTest.class);
	  private InterceptorEventPayloadProxy m_interceptorEvent;

	    private LambdaContext m_lambdaContext;
	    private Context m_context = mock(Context.class);
	    private ObjectMapper mapper = new ObjectMapper();
	    private static MockEnvVar envVar = new MockEnvVar();
	    static {
	        Map<String, String> env = new HashMap<>();
	        //Infusion soft configuration
	        env.put(Config.INFUSIONSOFT_API_NAME, "https://uf238.infusionsoft.com/api/xmlrpc");
	        env.put(Config.INFUSIONSOFT_API_KEY, "");
	        env.put(Config.INFUSION_ORDER_PROMO_CODE, "TIOwner");
	        //Simplybookme Configuration
	        env.put(Config.SIMPLY_BOOK_COMPANY_LOGIN, "phqviet93gmailcom");
	        env.put(Config.SIMPLY_BOOK_USER_NAME, "admin");
	        env.put(Config.SIMPLY_BOOK_PASSWORD, "");
	        env.put(Config.SIMPLY_BOOK_API_KEY, "");
	        env.put(Config.SIMPLY_BOOK_DEFAULT_USER_PASSWORD, "");
	        //AWS configuration
	        env.put(Config.AMAZON_ACCESS_KEY, "");
	        env.put(Config.AMAZON_SECRET_ACCESS_KEY, "");
	        env.put(Config.DYNAMODB_AWS_REGION, "");
	        
	        envVar.setValueSystems(env);
	    }
	    @Before
	    public void init() throws IOException {
	        // Initialize the external environment for testing locally
	        AmazonDynamoDB client = DynamodbUtils.getLocallyDynamoDB();
	        
	        ISExternalServiceBuilder builder = new ISExternalServiceImpl.ISExternalServiceBuilder();
	        DataServiceInf dataServiceInf = new DataServiceImpl();
			RecurringOrderInf recurringOrderInf = new RecurringOrderImpl(dataServiceInf);
			builder.setRecurringOrderInf(recurringOrderInf);
			
	        m_lambdaContext = LambdaContextImpl.builder()
	                .withClient(client)
	                .withEnvVar(envVar)
	                .withiSExternalService(new ISExternalServiceImpl(builder))
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
	    @Test
	    public void testCreatedInfusionSoftOrderIntegration() throws Exception {
	    	 AwsProxyRequest req = new AwsProxyRequest();
	    	 String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-inf.json"));
	         req.setBody(jsonString);
	         HashMap<String, String> event = new HashMap<>();
	         event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_CREATED);
	         req.setQueryStringParameters(event);
	         AwsProxyResponse response = m_interceptorEvent.handleRequest(req, m_context);
	         log.info(response.getBody());
	         OrderItem orderItem = mapper.readValue(response.getBody(), OrderItem.class);
	         Assert.assertNotNull(orderItem);
	    
	    }
	    @Test
	    public void testDeletedInfusionSoftOrderIntegration() throws Exception {
	    	 Context context = mock(Context.class);
	         AwsProxyRequest req = new AwsProxyRequest();
	         String jsonString = JsonUtils.getJsonString(this.getClass().getClassLoader().getResourceAsStream("order-payload-inf.json"));
	         req.setBody(jsonString);
	         HashMap<String, String> event = new HashMap<>();
	         event.put(EventType.EVENT_PARAMETER_NAME, EventType.ORDER_DELETED);
	         req.setQueryStringParameters(event);
	         AwsProxyResponse response = m_interceptorEvent.handleRequest(req, m_context);
	         log.info(response.getBody());
	         DeletedOrderResp delOrder = mapper.readValue(response.getBody(),DeletedOrderResp.class);
	         Assert.assertNotNull(delOrder);
	        
	         
	    }
	    
	    
}
