package com.tq.clickfunnel.lambda.handler;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.context.CFLambdaContextImpl;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.context.LambdaContextImpl;

/**
 * 
 * @author phqviet
 *
 */
public class InterceptorEventPayloadProxy implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = Logger.getLogger(InterceptorEventPayloadProxy.class);
    private LambdaContext m_lambdaContext;
    public InterceptorEventPayloadProxy() {
        this(new LambdaContextImpl());
    }
    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest req, Context context) {
        CFLambdaContext cfContext = initialize(context, m_lambdaContext); 
        String event = req.getQueryStringParameters().get(EventType.EVENT_PARAMETER_NAME);
        log.info("event=" + event);
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
}
