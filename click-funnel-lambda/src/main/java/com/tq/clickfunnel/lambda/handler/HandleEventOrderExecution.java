package com.tq.clickfunnel.lambda.handler;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.inf.exception.InfSDKExecption;

public abstract class HandleEventOrderExecution extends AbstractEventPayloadExecution {

    private static final Logger log = Logger.getLogger(HandleEventOrderExecution.class);

    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = null;
        try {
            CFOrderPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFOrderPayload.class);
            if (contactPayLoad == null)
                throw new CFLambdaException("Could not map Click funnel to purchase payload.");
            log.info("Request body " +input.getBody());
            resp = handleEventOrderLambda(input, contactPayLoad, cfLambdaContext);
     
        } catch (Exception e) {
            log.error(e);
            throw new CFLambdaException(e.getMessage(), e);
        }
        return resp;
    }
    

    protected abstract AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload orderPayload,
            CFLambdaContext cfLambdaContext) throws CFLambdaException, InfSDKExecption;
}
