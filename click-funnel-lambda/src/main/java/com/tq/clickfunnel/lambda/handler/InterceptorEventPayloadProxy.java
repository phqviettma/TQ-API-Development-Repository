package com.tq.clickfunnel.lambda.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.clickfunnel.lambda.impl.CFLambdaContextImpl;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;

/**
 * 
 * @author phqviet
 *
 */
public class InterceptorEventPayloadProxy  implements RequestHandler<AwsProxyRequest, AwsProxyResponse>  {

    private static final Logger log = LoggerFactory.getLogger(InterceptorEventPayloadProxy.class);
    private CFLambdaContext m_proxyContext;
    public InterceptorEventPayloadProxy() {
        this(new CFLambdaContextImpl());
    }
    
    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest req, Context context) {
        m_proxyContext.wrapAwsProxy(context);
        String event = req.getQueryStringParameters().get(EventType.EVENT_PARAMETER_NAME);
        log.info("event=", event);
        EventPayloadExecution execution = FactoryEventHandleExecution
                                            .standards()
                                            .ofExecution(EventCallback.on(event));
        return execution.execute(req, m_proxyContext);
    }
    
    // for UT only
    public InterceptorEventPayloadProxy(CFLambdaContext cfLambdaContext) {
        m_proxyContext = cfLambdaContext;
    }

    public CFLambdaContext getProxyContext() {
        return m_proxyContext;
    }

    public void setProxyContext(CFLambdaContext proxyContext) {
        this.m_proxyContext = proxyContext;
    }
}
