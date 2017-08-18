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
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.service.OrderItemService;

public class HandleEventDeletedOrderExecution extends HandleEventOrderExecution {
    public static final Logger log = Logger.getLogger(HandleEventDeletedOrderExecution.class);

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload orderPayload, CFLambdaContext cfLambdaContext)
            throws CFLambdaException {
        long start = System.currentTimeMillis();
        AwsProxyResponse resp = new AwsProxyResponse();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        OrderItemService orderItemService = lambdaContext.getOrderItemService();

        CFPurchase cfPurchase = orderPayload.getPurchase();
        Integer purchaseId = cfPurchase == null ? orderPayload.getId() : cfPurchase.getId();
        // 1. Get the purchase from database.
        OrderItem purchasedProduct = orderItemService.load(purchaseId);
        if (purchasedProduct == null)
            throw new CFLambdaException("The " + purchaseId + " has not bean purchased.");
        //2. Execute handle delete Infusion soft & Stripe integrations.
        List<CFProducts> cfProduct = (cfPurchase == null) ? orderPayload.getProducts() : cfPurchase.getProducts();
        String billingName = cfProduct.get(0).getBillingIntegration();
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
