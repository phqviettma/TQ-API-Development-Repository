package com.tq.clickfunnel.lambda.dynamodb.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.dynamodb.model.RecurringOrder;
import com.tq.clickfunnel.lambda.utils.CommonUtils;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.DataServiceImpl;
import com.tq.inf.impl.RecurringOrderImpl;
import com.tq.inf.service.DataServiceInf;
import com.tq.inf.service.RecurringOrderInf;

public class OrderItemServiceImplTest {
    public static final Logger log = LoggerFactory.getLogger(OrderItemServiceImplTest.class);

    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private OrderItemServiceImpl m_orderItemService = new OrderItemServiceImpl(m_amazonDynamoDB);

    @SuppressWarnings("rawtypes")
    @Test
    public void testPutOrderItemFromInfRecurringOrder() throws InfSDKExecption {
        DataServiceInf dataService = new DataServiceImpl();
        RecurringOrderInf recurringOrderInf = new RecurringOrderImpl(dataService);
        List<String> selectedFields = Arrays.asList("Id", "ContactId", "OriginatingOrderId", "ProductId", "StartDate", "EndDate",
                "LastBillDate", "NextBillDate", "Status", "AutoCharge", "CC1", "CC2", "MerchantAccountId");

        Object[] allRecuringOrder = recurringOrderInf.getAllRecurringOrder(Config.INFUSIONSOFT_API_NAME, Config.INFUSIONSOFT_API_KEY, 28,
                selectedFields);
        List<RecurringOrder> lsRecurringOrders = new LinkedList<>();
        for (Object obj : allRecuringOrder) {
            Map<?, ?> mapOrder = (Map) obj;
            RecurringOrder reOder = buildRecurringOrder(mapOrder);
            lsRecurringOrders.add(reOder);
        }
        OrderItem orderItem = new OrderItem().withEmail("dev12tma@gmail.com").withContactId(28).withRecurringOrders(lsRecurringOrders);
        m_orderItemService.put(orderItem);
    }

    @Test
    public void testLoadOrderItem() {
        OrderItem load = m_orderItemService.load("dev12tma@gmail.com");
        System.out.println(load);
    }

    private RecurringOrder buildRecurringOrder(Map<?, ?> mapOrder) {
        RecurringOrder reOder = new RecurringOrder().withId((Integer) mapOrder.get("Id")).withProductId((Integer) mapOrder.get("ProductId"))
                .withSubscriptionPlanId((Integer) mapOrder.get("SubscriptionPlanId")).withAutoCharge((Integer) mapOrder.get("AutoCharge"))
                .withOriginatingOrderId((Integer) mapOrder.get("OriginatingOrderId")).withStatus((String) mapOrder.get("Status"))
                .withStartDate(CommonUtils.formatDate(mapOrder.get("StartDate")))
                .withEndDate(CommonUtils.formatDate(mapOrder.get("EndDate")))
                .withNextBillDate(CommonUtils.formatDate(mapOrder.get("NextBillDate")))
                .withLastBillDate(CommonUtils.formatDate(mapOrder.get("LastBillDate")));
        return reOder;
    }

}
