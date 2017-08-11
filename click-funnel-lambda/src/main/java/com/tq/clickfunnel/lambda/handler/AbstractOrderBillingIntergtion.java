package com.tq.clickfunnel.lambda.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFProducts;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.OrderDetail;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
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
        //Handle creating order based on Billing integration
        return handleCreateBillingOrder(cfPurchase, contactItem, productItem, lambdaContext);
    }
    
    @Override
    public DeletedOrderResp deleteBilling(OrderItem orderItem, LambdaContext lambdaContext) throws CFLambdaException {
        EnvVar envVar = lambdaContext.getEnvVar();
        //1. Retrieve subscription to handle its delete
        Integer subscriptionId = getSubscriptionId(orderItem, lambdaContext);
        List<OrderDetail> orderDetails = orderItem.getOrderDetails();
        OrderDetail orderDetail = orderDetails.iterator().next();
        String apiName = envVar.getEnv(Config.INFUSIONSOFT_API_NAME);
        String apiKey = envVar.getEnv(Config.INFUSIONSOFT_API_KEY);
        //2. Delete Invoice associated with subscription first.
        deleteInvoiceFirst(orderDetail, apiName, apiKey, lambdaContext);
        //3. Delete the subscription after
        deleteSubscription(apiName, apiKey, subscriptionId, lambdaContext);
        OrderItemService orderItemService = lambdaContext.getOrderItemService();
        //4. Delete the already purchase order in DynamoDB
        orderItemService.delete(subscriptionId);
        DeletedOrderResp itemResp = buildResponseItem(orderDetail, subscriptionId);
        return itemResp;
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
    
    protected Boolean deleteInvoiceFirst(OrderDetail orderDetail, String apiName, String apiKey, LambdaContext lambdaContext) {
        InvoiceServiceInf invoiceServiceInf = lambdaContext.getInvoiceServiceInf();
        Boolean deletedInvoice;
        try {
            deletedInvoice = invoiceServiceInf.deleteInvoice(apiName, apiKey, orderDetail.getInvoiceInf());
            log.info("delete invoice " + deletedInvoice);
        } catch (InfSDKExecption e) {
            throw new CFLambdaException("The invoice " + orderDetail.getInvoiceInf() + " could not be deleted.", e);
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
