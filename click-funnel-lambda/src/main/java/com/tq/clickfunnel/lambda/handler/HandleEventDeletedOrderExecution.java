package com.tq.clickfunnel.lambda.handler;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.service.OrderItemService;

public class HandleEventDeletedOrderExecution extends HandleEventOrderExecution {
    public static final Logger log = Logger.getLogger(HandleEventDeletedOrderExecution.class);

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload contactPayLoad, CFLambdaContext cfLambdaContext)
            throws CFLambdaException {
        long start = System.currentTimeMillis();
        AwsProxyResponse resp = new AwsProxyResponse();
        CFPurchase purchase = contactPayLoad.getPurchase();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        OrderItemService orderItemService = lambdaContext.getOrderItemService();

        // 1. Get the purchase from database.
        OrderItem purchasedProduct = orderItemService.load(purchase.getId());
        if (purchasedProduct == null)
            throw new CFLambdaException("The " + purchase.getId() + " has not bean purchased.");
        //2. Execute handle delete Infusion soft & Stripe integrations.
        CFProducts cfProduct = purchase.getProducts().get(0);
        String billingName = cfProduct.getBillingIntegration();
        log.info("In deleting the " + billingName +" payment integration.");
        
        OrderBillingIntergtion billingIntegration = FactoryOrderBillingIntegration.getBillingIntegration(billingName);
        DeletedOrderResp itemResp = billingIntegration.deleteBilling(purchasedProduct, lambdaContext);
        itemResp.withPurchaseId(purchasedProduct.getPurchaseId());
        //3. Handle response
        handleResponse(input, resp, itemResp);
        log.info(String.format("deleteOrder()= %d ms", (System.currentTimeMillis() - start)));
        return resp;
    }
}
