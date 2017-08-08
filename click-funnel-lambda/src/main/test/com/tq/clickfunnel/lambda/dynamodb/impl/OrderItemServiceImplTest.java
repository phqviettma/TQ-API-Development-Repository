package com.tq.clickfunnel.lambda.dynamodb.impl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;

public class OrderItemServiceImplTest {
    public static final Logger log = LoggerFactory.getLogger(OrderItemServiceImplTest.class);

    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private OrderItemServiceImpl m_orderItemService = new OrderItemServiceImpl(m_amazonDynamoDB);

    @Test
    public void testLoadOrderItem() {
        OrderItem load = m_orderItemService.load("dev12tma@gmail.com");
        System.out.println(load);
    }
}
