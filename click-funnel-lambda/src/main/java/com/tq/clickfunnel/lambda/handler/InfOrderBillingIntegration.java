package com.tq.clickfunnel.lambda.handler;

import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.OrderItem;

public class InfOrderBillingIntegration extends AbstractOrderBillingIntergtion{

    @Override
    public OrderItem createBilling(CFOrderPayload cfPurchase, LambdaContext lambdaContext) throws CFLambdaException{

        return null;
    }

    @Override
    public DeletedOrderResp deleteBilling(OrderItem orderItem, LambdaContext lambdaContext) throws CFLambdaException{
        return null;
    }

}
