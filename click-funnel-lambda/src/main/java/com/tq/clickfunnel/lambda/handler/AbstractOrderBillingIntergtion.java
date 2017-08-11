package com.tq.clickfunnel.lambda.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;

public abstract class AbstractOrderBillingIntergtion implements OrderBillingIntergtion{
    public static final Logger log = Logger.getLogger(AbstractOrderBillingIntergtion.class);

    protected ContactItem loadContactAtDB(String email, LambdaContext lambdaContext) {
        ContactItem contactItem = lambdaContext.getContactItemService().load(email);
        if (contactItem == null)
            throw new CFLambdaException(email + " not found.");
        return contactItem;
    }

    protected ProductItem loadProductAtDB(CFOrderPayload contactPayLoad, CFPurchase purchase, LambdaContext lambdaContext) {
        List<CFProducts> products = purchase.getProducts();
        if (products == null || products.isEmpty()) {
            throw new CFLambdaException("No any products is purchased with " + purchase.getId() + " identification.");
        }
        Integer cfProudctionID = products.get(0).getId();
        ProductItem productItem = lambdaContext.getProductItemService().load(cfProudctionID);
        if (productItem == null)
            throw new CFLambdaException(cfProudctionID + " not found.");
        return productItem;
    }
}
