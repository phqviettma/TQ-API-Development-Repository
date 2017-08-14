package com.tq.clickfunnel.lambda.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFPurchase;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.INFProduct;
import com.tq.common.lambda.dynamodb.model.OrderDetail;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.query.OrderQuery;
import com.tq.inf.service.OrderServiceInf;
import com.tq.inf.service.RecurringOrderInf;

/**
 * 
 * @author phqviet When Click Funnel integrated Stripe in the Payment Integration for product, we will create the Order under the contact ID and with the mapped
 *         Product ID.
 *
 */
public class StripeOrderBillingIntergration extends AbstractOrderBillingIntergtion {

    private static final Logger log = Logger.getLogger(StripeOrderBillingIntergration.class);

    @Override
    public OrderItem handleCreateBillingOrder(CFPurchase cfPurchase, ContactItem contactItem, ProductItem productItem,
            LambdaContext lambdaContext) {
        return addOrderToInf(contactItem, productItem, cfPurchase, lambdaContext);
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
        deleteInvoiceFirst(orderDetail.getInvoiceInf(), apiName, apiKey, lambdaContext);
        //3. Delete the subscription after
        deleteSubscription(apiName, apiKey, subscriptionId, lambdaContext);
        OrderItemService orderItemService = lambdaContext.getOrderItemService();
        //4. Delete the already purchase order in DynamoDB
        orderItemService.delete(orderItem);
        DeletedOrderResp itemResp = buildResponseItem(orderDetail, subscriptionId);
        return itemResp;
    }

    @Override
    protected Integer getSubscriptionId(OrderItem orderItem, LambdaContext lambdaContext) {
        List<OrderDetail> orderDetails = orderItem.getOrderDetails();
        OrderDetail orderDetail = orderDetails.iterator().next();
        RecurringOrderInf recurringOrderInf = lambdaContext.getRecurringOrderInf();
        EnvVar envVar = lambdaContext.getEnvVar();
        String apiName = envVar.getEnv(Config.INFUSIONSOFT_API_NAME);
        String apiKey = envVar.getEnv(Config.INFUSIONSOFT_API_KEY);
        // Retrieve subscription to handle its delete
        return retrieveRecurringOrder(orderDetail, recurringOrderInf, apiName, apiKey);
    }

    private OrderItem addOrderToInf(ContactItem contactItem, ProductItem productItem, CFPurchase purchase, LambdaContext lambdaContext) {
        long start = System.currentTimeMillis();
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
        log.info(String.format("addOrderToInf()= %d ms", (System.currentTimeMillis() - start)));
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

        OrderDetail orderDtail = new OrderDetail().withInvoiceInf(invoiceId).withOrderIdInf(orderId) // Infusion soft response
                .withMessage(message).withRefNum(refNum).withContactId(client.getContactId()).withMessage(message)
                .withSuccessful(successful).withCreatedAt(createdAt).withUpdatedAt(createdAt).withProductIdInf(infProduct.getProductIds())
                .withProductCf(productItem.getCfProduct().getId()).withCode(code);
        return orderDtail;
    }

    private OrderQuery buildOrderQuery(Integer contactId, INFProduct infProduct, String promoCode) {
        OrderQuery orderQuery = new OrderQuery().withContactID(contactId).withCardID(infProduct.getCartId())
                .withPlanID(infProduct.getPlanId()).withProductionIDs(infProduct.getProductIds())
                .withSubscriptionIDs(infProduct.getSubscriptionPlanIds()).withPromoCodes(Arrays.asList(promoCode));
        return orderQuery;
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
}
