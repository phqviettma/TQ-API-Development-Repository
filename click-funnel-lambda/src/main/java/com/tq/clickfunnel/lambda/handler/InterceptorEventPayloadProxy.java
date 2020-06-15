package com.tq.clickfunnel.lambda.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.context.CFLambdaContextImpl;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.ApplyTagQuery;

/**
 * 
 * @author phqviet
 *
 */
public class InterceptorEventPayloadProxy implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = Logger.getLogger(InterceptorEventPayloadProxy.class);
    private LambdaContext m_lambdaContext;
    protected ObjectMapper m_mapper = new ObjectMapper();
    public InterceptorEventPayloadProxy() {
        this(new LambdaContextImpl());
    }
    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest req, Context context) {
        CFLambdaContext cfContext = initialize(context, m_lambdaContext); 
        String event = req.getQueryStringParameters().get(EventType.EVENT_PARAMETER_NAME);
        log.info("event=" + event);
        if ("applyTagFromButton".equalsIgnoreCase(event)) {
        	return applyTagFromButton(req, cfContext);
        }
        EventPayloadExecution execution = FactoryEventHandleExecution
                .standards().ofExecution(EventCallback.on(event));
        return execution.execute(req, cfContext);
    }

    // For only Unit test
    public InterceptorEventPayloadProxy(LambdaContext lambdaContext) {
        m_lambdaContext = lambdaContext;
    }
    protected CFLambdaContext initialize(Context context,  LambdaContext lambdaContext) {
        return new CFLambdaContextImpl(context, lambdaContext);
    }
    
    public boolean applyTagToInfusionsoft(LambdaContext lambdaContext,Integer contactId, Object appliedTagId) throws InfSDKExecption {
        if (appliedTagId == null) {
            log.info("There are no Tag ID being provided.");
            return false;
        }
        EnvVar envVar = lambdaContext.getEnvVar();
        ApplyTagQuery applyTagQuery = new ApplyTagQuery().withContactID(contactId).withTagID(Integer.valueOf(appliedTagId+""));
        boolean result = lambdaContext.getContactServiceInf().applyTag(envVar.getEnv(Config.INFUSIONSOFT_API_NAME),
                envVar.getEnv(Config.INFUSIONSOFT_API_KEY), applyTagQuery);
        log.info(String.format("Result [%s] response when applied Tag[%s] to Contact ID [%s]", result, appliedTagId, contactId));
        return result;
    }
    
    public AwsProxyResponse applyTagFromButton(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
    	AwsProxyResponse resp = new AwsProxyResponse();
    	try {
			log.info("Action event: Apply tag from button.");
	    	LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
	    	
			String email = input.getQueryStringParameters().get("email");
			String appliedTagId = input.getQueryStringParameters().get("eventTypeName");
			
			ContactItem contactItem = lambdaContext.getContactItemService().load(email);
			Integer contactId = contactItem.getClient().getContactId();
			log.info("contactId: " + contactId);
			
			applyTagToInfusionsoft(lambdaContext, contactId, appliedTagId);
			log.info("Apply tag completed for contactId: " + String.valueOf(contactId));
    		
			resp = buildResponseForApplyTag(input);
		} catch (Exception e) {
			log.info(e);
            log.error("", e);
            Map<String, String> headers = new HashMap<String, String>();
    		headers.put("Access-Control-Allow-Origin", "*");
    		headers.put("content-Type", "application/json");
            String rebuild = String.format("{\"error\": \"%s\"}", e);
            e.printStackTrace();
            resp.setBody(rebuild);
            resp.setHeaders(headers);
            resp.setStatusCode(503);
            return resp;
		}
    	return resp;
    }
    
    private AwsProxyResponse buildResponseForApplyTag(AwsProxyRequest input) {
    	AwsProxyResponse resp = new AwsProxyResponse();
    	Map<String, String> headers = new HashMap<String, String>();
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("content-Type", "application/json");
    	String rebuild = String.format("{\"status\": \"success\"}");
    	resp.setBody(rebuild);
        resp.setHeaders(headers);
        resp.setStatusCode(200);
    	return resp;
    }
}
