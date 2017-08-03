package com.tq.clickfunnel.lambda.dynamodb.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.configuration.Config;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderInf;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;

public class OrderItemServiceImplTest {
    public static final Logger log = LoggerFactory.getLogger(OrderItemServiceImplTest.class);

    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private OrderItemServiceImpl m_orderItemService = new OrderItemServiceImpl(m_amazonDynamoDB);
    
    @Test
    public void testPutOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setEmail("abc@tma.com.vn");
        List<OrderInf> infOrders = new LinkedList<>();
        OrderInf or1 = new OrderInf();
        or1.setContactId(1);
        or1.setEmail("abc@tma.com.vn");
        or1.setInvoiceId(1000);
        or1.setOrderId(2000);
        or1.setProductId(3000);
        String createdAt = Config.DATE_FORMAT_24_H.format(new Date());
        or1.setCreatedAt(createdAt);
        or1.setUpdatedAt(createdAt);
        
        OrderInf or2 = new OrderInf();
        or2.setContactId(1);
        or2.setEmail("abc@tma.com.vn");
        or2.setInvoiceId(1500);
        or2.setOrderId(2500);
        or2.setProductId(3000);
        createdAt = Config.DATE_FORMAT_24_H.format(new Date());
        or2.setCreatedAt(createdAt);
        or2.setUpdatedAt(createdAt);
        
        infOrders.add(or1);
        infOrders.add(or2);
        orderItem.setInfOrders(infOrders );
        
        m_orderItemService.putItem(orderItem);
    }
}
