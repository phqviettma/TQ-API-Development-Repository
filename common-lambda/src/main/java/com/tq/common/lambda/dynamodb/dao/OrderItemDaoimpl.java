package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class OrderItemDaoimpl extends AbstractItem<OrderItem, Integer> implements OrderItemDao {

    public OrderItemDaoimpl(AmazonDynamoDB client) {
        super(client, OrderItem.class);
    }

    @Override
    public OrderItem loadItem(Integer pruchaseId) {
        return super.loadItem(pruchaseId);
    }
}
