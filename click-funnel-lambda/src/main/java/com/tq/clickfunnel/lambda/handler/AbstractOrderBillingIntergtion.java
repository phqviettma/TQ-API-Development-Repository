package com.tq.clickfunnel.lambda.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.OrderDetail;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.InvoiceServiceInf;

public abstract class AbstractOrderBillingIntergtion implements OrderBillingIntergtion{
    public static final Logger log = Logger.getLogger(AbstractOrderBillingIntergtion.class);
    
    @Override
    public OrderItem createBilling(CFOrderPayload orderPayload, LambdaContext lambdaContext) throws CFLambdaException {
        CFPurchase cfPurchase = orderPayload.getPurchase();
        CFContact contact = cfPurchase.getContact();
        // 1. Load the contact already existed in DynamoDB based on email
        ContactItem contactItem = loadContactAtDB(contact.getEmail(), lambdaContext);
        // 2. Load Product in DynamoDB that contact is being purchased.
        ProductItem productItem = loadProductAtDB(orderPayload, cfPurchase, lambdaContext);
        //3. Handle for creating order based on Billing integration ( Infusion soft, Stripe )
        return handleCreateBillingOrder(cfPurchase, contactItem, productItem, lambdaContext);
    }
    
    protected abstract Integer getSubscriptionId(OrderItem orderItem, LambdaContext lambdaContext);

    public abstract OrderItem handleCreateBillingOrder(CFPurchase cfPurchase, ContactItem contactItem, ProductItem productItem, LambdaContext lambdaContext);
    
    
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
    
    protected Boolean deleteInvoiceFirst(Integer invoiceId, String apiName, String apiKey, LambdaContext lambdaContext) {
        InvoiceServiceInf invoiceServiceInf = lambdaContext.getInvoiceServiceInf();
        Boolean deletedInvoice;
        try {
            deletedInvoice = invoiceServiceInf.deleteInvoice(apiName, apiKey, invoiceId);
            log.info("delete invoice " + deletedInvoice);
        } catch (InfSDKExecption e) {
            throw new CFLambdaException("The invoice " + invoiceId + " could not be deleted.", e);
        }
        return deletedInvoice;
    }
    
    protected Boolean deleteSubscription(String apiName, String apiKey, Integer subscriptionId, LambdaContext lambdaContext) {
        InvoiceServiceInf invoiceServiceInf = lambdaContext.getInvoiceServiceInf();
        Boolean deleteSubscription;
        try {
            deleteSubscription = invoiceServiceInf.deleteSubscription(apiName, apiKey, subscriptionId);
            log.info("delete subscription " + deleteSubscription);
        } catch (InfSDKExecption e) {
            throw new CFLambdaException("Could not delete subcription " + subscriptionId + " due to " + e.getMessage(), e);
        }
        return deleteSubscription;
    }
    
    protected DeletedOrderResp buildResponseItem(OrderDetail orderDetail, Integer subscriptionId) {
        DeletedOrderResp itemResp = new DeletedOrderResp()
                .withContactId(orderDetail.getContactId())
                .withInvoiceId(orderDetail.getInvoiceInf())
                .withSubscriptionId(subscriptionId);
        return itemResp;
    }
}
