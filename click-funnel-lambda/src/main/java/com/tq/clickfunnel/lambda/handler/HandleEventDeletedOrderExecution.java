package com.tq.clickfunnel.lambda.handler;

import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.OrderDetail;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.InvoiceServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public class HandleEventDeletedOrderExecution extends HandleEventOrderExecution {
    public static final Logger log = Logger.getLogger(HandleEventDeletedOrderExecution.class);

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload contactPayLoad, CFLambdaContext cfLambdaContext)
            throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        CFPurchase purchase = contactPayLoad.getPurchase();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        OrderItemService orderItemService = lambdaContext.getOrderItemService();
        OrderItem purchasedProduct = orderItemService.load(purchase.getId());
        // 1. Get the purchase from database.
        if (purchasedProduct == null)
            throw new CFLambdaException("The " + purchase.getId() + " has not bean purchased.");

        // 2. Handle infusion soft subscription.
        EnvVar envVar = lambdaContext.getEnvVar();
        OrderDetail orderDetail = purchasedProduct.getOrderDetails().get(0);
        RecurringOrderInf recurringOrderInf = lambdaContext.getRecurringOrderInf();
        String apiName = envVar.getEnv(Config.INFUSIONSOFT_API_NAME);
        String apiKey = envVar.getEnv(Config.INFUSIONSOFT_API_KEY);

        // 2.1 Retrieve subscription to handle its delete
        Integer subscriptionId = retrieveRecurringOrder(orderDetail, recurringOrderInf, apiName, apiKey);

        // 2.2 delete Invoice associated with subscription first.
        InvoiceServiceInf invoiceServiceInf = lambdaContext.getInvoiceServiceInf();
        deleteInvoiceFirst(invoiceServiceInf, orderDetail, apiName, apiKey);

        // 2.3. Delete the subscription after
        readyToDeleteSubscription(apiName, apiKey, subscriptionId, invoiceServiceInf);

        // 3: Delete the already purchase order in Dynamodb
        orderItemService.delete(subscriptionId);

        DeletedOrderResp itemResp = buildResponseItem(purchase, orderDetail, subscriptionId);
        handleResponse(input, resp, itemResp);
        return resp;
    }

    private DeletedOrderResp buildResponseItem(CFPurchase purchase, OrderDetail orderDetail, Integer subscriptionId) {
        DeletedOrderResp itemResp = new DeletedOrderResp()
                .withContactId(orderDetail.getContactId())
                .withInvoiceId(orderDetail.getInvoiceInf())
                .withSubscriptionId(subscriptionId).withPurchaseId(purchase.getId());
        return itemResp;
    }

    private Integer readyToDeleteSubscription(String apiName, String apiKey, Integer subscriptionId, InvoiceServiceInf invoiceServiceInf) {
        Integer deleteSubscription;
        try {
            deleteSubscription = invoiceServiceInf.deleteSubscription(apiName, apiKey, subscriptionId);
        } catch (InfSDKExecption e) {
            throw new CFLambdaException("Could not delete subcription " + subscriptionId + " due to " + e.getMessage(), e);
        }
        return deleteSubscription;
    }

    private Integer retrieveRecurringOrder(OrderDetail orderDetail, RecurringOrderInf recurringOrderInf, String apiName, String apiKey) {
        Object[] subcruptions;
        Integer subscriptionId = null;
        try {
            subcruptions = recurringOrderInf.getRecurringOrderFromOriginatingOrderId(apiName, apiKey, orderDetail.getContactId(),
                    orderDetail.getInvoiceInf(), Arrays.asList("Id"));
            if (subcruptions == null || subcruptions.length == 0) {
                throw new CFLambdaException(String.format("Could not find the subscription with invoice %d in contact %d ",
                        orderDetail.getInvoiceInf(), orderDetail.getContactId()));
            }
            subscriptionId = (Integer) ((Map<?, ?>) subcruptions[0]).get("Id");
        } catch (InfSDKExecption e) {
            log.error("Could not retrieve the Recurring Order with " + orderDetail.getInvoiceInf());
            throw new CFLambdaException(e.getMessage(), e);
        }
        return subscriptionId;
    }

    private Integer deleteInvoiceFirst(InvoiceServiceInf invoiceServiceInf, OrderDetail orderDetail, String apiName, String apiKey) {
        Integer deletedInvoice;
        try {
            deletedInvoice = invoiceServiceInf.deleteInvoice(apiName, apiKey, orderDetail.getInvoiceInf());
        } catch (InfSDKExecption e) {
            throw new CFLambdaException("The invoice " + orderDetail.getInvoiceInf() + " could not be deleted.", e);
        }
        return deletedInvoice;
    }

}
