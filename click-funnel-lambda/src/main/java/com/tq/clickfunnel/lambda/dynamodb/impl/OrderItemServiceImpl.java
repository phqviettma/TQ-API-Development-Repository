package com.tq.clickfunnel.lambda.dynamodb.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;
import com.tq.clickfunnel.lambda.dynamodb.service.AbstractItem;
import com.tq.clickfunnel.lambda.dynamodb.service.OrderItemService;

public class OrderItemServiceImpl extends AbstractItem<OrderItem, String> implements OrderItemService {

    public OrderItemServiceImpl(AmazonDynamoDB client) {
        super(client, OrderItem.class);
    }

    @Override
    public boolean put(OrderItem orderItem) {
        putItem(orderItem);
        return true;
    }

    @Override
    public OrderItem load(String email) {
        return load(email);
    }
}
