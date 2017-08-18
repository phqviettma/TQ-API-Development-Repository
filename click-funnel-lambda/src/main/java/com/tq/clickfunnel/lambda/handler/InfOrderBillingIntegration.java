package com.tq.clickfunnel.lambda.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.resp.DeletedOrderResp;
import com.tq.common.lambda.config.Config;
import com.tq.common.lambda.config.EnvVar;
import com.tq.common.lambda.context.LambdaContext;
import com.tq.common.lambda.dynamodb.model.ClientInfo;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.model.RecurringOrder;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.utils.Utils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.RecurringOrderInf;

/**
 * @author phqviet In Integrating Infusion soft in Payment Billing of Click funnel. CLick funnel will automatically create the Order under the email contact,
 *         So, in this case, we will get that Order and save for handling in further.
 */
public class InfOrderBillingIntegration extends AbstractOrderBillingIntergtion {

    @Override
    public OrderItem handleCreateBillingOrder(Integer purchaseId, ContactItem contactItem, ProductItem productItem,
            LambdaContext lambdaContext) {
        RecurringOrder recurringOrder = retrieveLatestSubscriptionOrder(contactItem, productItem, lambdaContext);
        OrderItem orderItem = new OrderItem().withEmail(contactItem.getEmail()).withPurchaseId(purchaseId)
                .withRecurringOrder(recurringOrder);
        return orderItem;
    }
    
    @Override
    public DeletedOrderResp deleteBilling(OrderItem orderItem, LambdaContext lambdaContext) throws CFLambdaException {
        EnvVar envVar = lambdaContext.getEnvVar();
        //1. Retrieve subscription to handle its delete
        Integer subscriptionId = getSubscriptionId(orderItem, lambdaContext);
        RecurringOrder recurringOrder = orderItem.getRecurringOrder();
        String apiName = envVar.getEnv(Config.INFUSIONSOFT_API_NAME);
        String apiKey = envVar.getEnv(Config.INFUSIONSOFT_API_KEY);
        
        //2. Delete Invoice associated with subscription first.
        deleteInvoiceFirst(recurringOrder.getOriginatingOrderId(), apiName, apiKey, lambdaContext);
        
        //3. Delete the subscription after
        deleteSubscription(apiName, apiKey, subscriptionId, lambdaContext);
        OrderItemService orderItemService = lambdaContext.getOrderItemService();
        
        //4. Delete the already purchase order in DynamoDB
        orderItemService.delete(orderItem);
        DeletedOrderResp itemResp = new DeletedOrderResp()
                .withPurchaseId(orderItem.getPurchaseId())
                .withContactId(recurringOrder.getContactId())
                .withInvoiceId(recurringOrder.getOriginatingOrderId())
                .withSubscriptionId(subscriptionId);
        return itemResp;
    }

    @Override
    protected Integer getSubscriptionId(OrderItem orderItem, LambdaContext lambdaContext) {
        return orderItem.getRecurringOrder().getId();
    }

    private RecurringOrder retrieveLatestSubscriptionOrder(ContactItem contactItem, ProductItem productItem, LambdaContext lambdaContext) {
        EnvVar envVar = lambdaContext.getEnvVar();
        String apiName = envVar.getEnv(Config.INFUSIONSOFT_API_NAME);
        String apiKey = envVar.getEnv(Config.INFUSIONSOFT_API_KEY);
        RecurringOrderInf recurringOrderInf = lambdaContext.getRecurringOrderInf();
        ClientInfo client = contactItem.getClient();
        List<Integer> productIds = productItem.getInfProduct().getProductIds();
        List<String> selectedFields = Arrays.asList("Id", "ContactId", "OriginatingOrderId", "ProductId", "StartDate", "EndDate",
                "LastBillDate", "NextBillDate", "Status", "AutoCharge", "CC1", "CC2", "MerchantAccountId");
        RecurringOrder recurringOrder = null;
        try {
            Map<?, ?> latestSubscriptionOrder = (Map<?, ?>) recurringOrderInf.getLatestRecurringOrderFromProduct(apiName, apiKey, client.getContactId(),
                    productIds.get(0), selectedFields);
            if (latestSubscriptionOrder == null)
                throw new CFLambdaException(String.format("Could not found the latest order of %d product under the contact %d .",
                        productIds.get(0), client.getContactId()));
            recurringOrder = buildRecurringOrder(latestSubscriptionOrder, client.getContactId());
        } catch (InfSDKExecption e) {
            throw new CFLambdaException(e.getMessage(), e);
        }
        return recurringOrder;
    }

    private RecurringOrder buildRecurringOrder(Map<?, ?> mapOrder, Integer contactId) {
        RecurringOrder reOder = new RecurringOrder().withId((Integer) mapOrder.get("Id")).withProductId((Integer) mapOrder.get("ProductId"))
                .withContactId(contactId)
                .withSubscriptionPlanId((Integer) mapOrder.get("SubscriptionPlanId")).withAutoCharge((Integer) mapOrder.get("AutoCharge"))
                .withOriginatingOrderId((Integer) mapOrder.get("OriginatingOrderId")).withStatus((String) mapOrder.get("Status"))
                .withStartDate(Utils.formatDate(mapOrder.get("StartDate"))).withEndDate(Utils.formatDate(mapOrder.get("EndDate")))
                .withNextBillDate(Utils.formatDate(mapOrder.get("NextBillDate")))
                .withLastBillDate(Utils.formatDate(mapOrder.get("LastBillDate")));
        return reOder;
    }
}
