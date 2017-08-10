package com.tq.clickfunnel.lambda.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;

public abstract class HandleEventOrderExecution extends AbstractEventPayloadExecution {

    private static final Logger log = Logger.getLogger(HandleEventOrderExecution.class);

    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = null;
        try {
            CFOrderPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFOrderPayload.class);
            if (contactPayLoad == null)
                throw new CFLambdaException("Could not map Click funnel to purchase payload.");
            resp = handleEventOrderLambda(input, contactPayLoad, cfLambdaContext);
        } catch (Exception e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        return resp;
    }

    protected abstract AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload contactPayLoad,
            CFLambdaContext cfLambdaContext) throws CFLambdaException;

    protected ContactItem loadContactAtDB(String email, LambdaContext lambdaContext) {
        ContactItem contactItem = lambdaContext.getContactItemService().load(email);
        if (contactItem == null)
            throw new CFLambdaException(email + " not found.");
        return contactItem;
    }

    protected ProductItem loadProductAtDB(CFOrderPayload contactPayLoad, CFPurchase purchase, LambdaContext lambdaContext) {
        List<CFProducts> products = purchase.getProducts();
        if (products == null || products.isEmpty()) {
            log.info(contactPayLoad);
            throw new CFLambdaException("The contact has not purchased any products.");
        }
        Integer cfProudctionID = products.get(0).getId();
        ProductItem productItem = lambdaContext.getProductItemService().load(cfProudctionID);
        if (productItem == null)
            throw new CFLambdaException(cfProudctionID + " not found.");
        return productItem;
    }
}
