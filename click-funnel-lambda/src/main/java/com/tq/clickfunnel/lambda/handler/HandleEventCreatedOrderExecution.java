package com.tq.clickfunnel.lambda.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.context.CFLambdaContext;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.INFProduct;
import com.tq.common.lambda.dynamodb.model.OrderDetail;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.OrderServiceInf;

public class HandleEventCreatedOrderExecution extends HandleEventOrderExecution {

    @Override
    protected AwsProxyResponse handleEventOrderLambda(AwsProxyRequest input, CFOrderPayload contactPayLoad,
            CFLambdaContext cfLambdaContext) {
        AwsProxyResponse resp = new AwsProxyResponse();
        LambdaContext lambdaContext = cfLambdaContext.getLambdaContext();
        CFPurchase purchase = contactPayLoad.getPurchase();
        CFContact contact = purchase.getContact();

        // 1. Load the contact already existed in DynamoDB based on email
        ContactItem contactItem = loadContactAtDB(contact.getEmail(), lambdaContext);

        // 2. Load Product in DynamoDB that contact is being purchased.
        ProductItem productItem = loadProductAtDB(contactPayLoad, purchase, lambdaContext);

        // 3. Create Order under email on infusion soft
        OrderItem addOrder = addOrderToInf(contactItem, productItem, purchase, lambdaContext);

        // 4. Save the Order to DynamoDB for handling in further
        if (addOrder != null) {
            lambdaContext.getOrderItemService().put(addOrder);
        }
        // 4. handle successfully
        handleResponse(input, resp, addOrder);
        return resp;
    }

    private OrderItem addOrderToInf(ContactItem contactItem, ProductItem productItem, CFPurchase purchase, LambdaContext lambdaContext) {
        OrderItem orderItem = null;
        ClientInfo client = contactItem.getClient();
        Integer contactId = client.getContactId();
        try {
            EnvVar envVar = lambdaContext.getEnvVar();
            INFProduct infProduct = productItem.getInfProduct();
            String promoCode = lambdaContext.getEnvVar().getEnv(Config.INFUSION_ORDER_PROMO_CODE);
            OrderQuery orderQuery = buildOrderQuery(contactId, infProduct, promoCode);
            OrderServiceInf orderServiceInf = lambdaContext.getOrderServiceInf();
            Map<?, ?> order = (Map<?, ?>) orderServiceInf.addOrder(envVar.getEnv(Config.INFUSIONSOFT_API_NAME),
                    envVar.getEnv(Config.INFUSIONSOFT_API_KEY), orderQuery);
            if (order == null)
                return null;
            OrderDetail orderDtail = buildOrderDetail(client, productItem, infProduct, order);
            orderItem = new OrderItem().withPurchaseId(purchase.getId()) // as hash key
                    .withEmail(client.getEmail()).withOrderDetails(Arrays.asList(orderDtail));

        } catch (Exception e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        return orderItem;
    }

    private OrderDetail buildOrderDetail(ClientInfo client, ProductItem productItem, INFProduct infProduct, Map<?, ?> order) {
        Integer orderId = Integer.valueOf((String) order.get("OrderId"));
        Integer invoiceId = Integer.valueOf((String) order.get("InvoiceId"));
        String refNum = (String) order.get("RefNum");
        String message = (String) order.get("Message");
        String successful = (String) order.get("Successful");
        String code = (String) order.get("Code");
        String createdAt = Config.DATE_FORMAT_24_H.format(new Date());

        OrderDetail orderDtail = new OrderDetail()
                .withInvoiceInf(invoiceId).withOrderIdInf(orderId) // Infusion soft response
                .withMessage(message).withRefNum(refNum)
                .withContactId(client.getContactId())
                .withMessage(message).withSuccessful(successful).withCreatedAt(createdAt).withUpdatedAt(createdAt)
                .withProductIdInf(infProduct.getProductIds()).withProductCf(productItem.getCfProduct().getId())
                .withCode(code);
        return orderDtail;
    }

    private OrderQuery buildOrderQuery(Integer contactId, INFProduct infProduct, String promoCode) {
        OrderQuery orderQuery = new OrderQuery().withContactID(contactId).withCardID(infProduct.getCartId())
                .withPlanID(infProduct.getPlanId()).withProductionIDs(infProduct.getProductIds())
                .withSubscriptionIDs(infProduct.getSubscriptionPlanIds()).withPromoCodes(Arrays.asList(promoCode));
        return orderQuery;
    }
}
