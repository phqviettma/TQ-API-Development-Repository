package com.tq.clickfunnel.lambda.handler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.ClientInfo;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.dynamodb.model.RecurringOrder;
import com.tq.clickfunnel.lambda.exception.CFLambdaException;
import com.tq.clickfunnel.lambda.modle.CFContact;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;
import com.tq.clickfunnel.lambda.service.CFLambdaContext;
import com.tq.clickfunnel.lambda.service.CFLambdaService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;
import com.tq.clickfunnel.lambda.utils.CommonUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.service.RecurringOrderInf;

public class HandleEventCreatedOrderExecution extends HandleEventOrderExecution {
    private static final Logger log = LoggerFactory.getLogger(HandleEventCreatedOrderExecution.class);
    private CFLambdaService m_cfLambdaService;
    private CFLambdaServiceRepository m_cfServiceRepo;

    @Override
    public AwsProxyResponse handleLambdaProxy(AwsProxyRequest input, CFLambdaContext cfLambdaContext) throws CFLambdaException {
        AwsProxyResponse resp = new AwsProxyResponse();
        m_cfLambdaService = cfLambdaContext.getCFLambdaService();
        m_cfServiceRepo = cfLambdaContext.getCFLambdaServiceRepository();
        OrderItem addOrder = null;
        try {
            CFOrderPayload contactPayLoad = m_mapper.readValue(input.getBody(), CFOrderPayload.class);
            if (contactPayLoad != null) {
                CFContact contact = contactPayLoad.getPurchase().getContact();

                // 1. Load the contact already existed in DynamoDB based on email
                ContactItem contactItem = loadContactAtDB(contact.getEmail(), m_cfServiceRepo);
                /**
                 * 2. Create Order under email on Infusion soft. We already configured on Click funnel to handle for adding new Order to infusion soft via
                 * Payment Integration so here, we just need to get the new order to save into database for further.
                 */
                addOrder = retrieveOrderItem(contactItem);
                // 3. Save the Order to DynamoDB for handling in further
                if (addOrder != null) {
                    m_cfServiceRepo.getOrderItemService().put(addOrder);
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new CFLambdaException(e.getMessage(), e);
        }
        // 5. handle successfully
        handleResponse(input, resp, addOrder);
        return resp;
    }

    @SuppressWarnings("rawtypes")
    private OrderItem retrieveOrderItem(ContactItem contactItem) {
        OrderItem orderItem = null;
        try {
            RecurringOrderInf recurringOrderInf = m_cfLambdaService.getRecurringOrderInf();

            ClientInfo client = contactItem.getClient();
            List<String> selectedFields = Arrays.asList("Id", "ContactId", "OriginatingOrderId", "ProductId", "StartDate", "EndDate",
                    "LastBillDate", "NextBillDate", "Status", "AutoCharge", "SubscriptionPlanId");
            Object[] recurringOrders = recurringOrderInf.getAllRecurringOrder(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY,
                    client.getContactId(), selectedFields);
            if (recurringOrders == null || recurringOrders.length == 0)
                return null;

            List<RecurringOrder> lsRecurringOrders = new LinkedList<>();
            for (Object obj : recurringOrders) {
                Map<?, ?> mapOrder = (Map) obj;
                RecurringOrder reOder = buildRecurringOrder(mapOrder);
                lsRecurringOrders.add(reOder);
            }
            orderItem = new OrderItem()
                    .withEmail(client.getEmail()).withContactId(client.getContactId())
                    .withRecurringOrders(lsRecurringOrders);
        } catch (InfSDKExecption e) {
            new CFLambdaException(e.getMessage(), e);
        }
        return orderItem;
    }

    private RecurringOrder buildRecurringOrder(Map<?, ?> mapOrder) {
        RecurringOrder reOder = new RecurringOrder().withId((Integer) mapOrder.get("Id"))
                .withProductId((Integer) mapOrder.get("ProductId"))
                .withSubscriptionPlanId((Integer) mapOrder.get("SubscriptionPlanId"))
                .withAutoCharge((Integer) mapOrder.get("AutoCharge"))
                .withOriginatingOrderId((Integer) mapOrder.get("OriginatingOrderId"))
                .withStatus((String) mapOrder.get("Status"))
                .withStartDate(CommonUtils.formatDate(mapOrder.get("StartDate")))
                .withEndDate(CommonUtils.formatDate(mapOrder.get("EndDate")))
                .withNextBillDate(CommonUtils.formatDate(mapOrder.get("NextBillDate")))
                .withLastBillDate(CommonUtils.formatDate(mapOrder.get("LastBillDate")));
        return reOder;
    }

}
