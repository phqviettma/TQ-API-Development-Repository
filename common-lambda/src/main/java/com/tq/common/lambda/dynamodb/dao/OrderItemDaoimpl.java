package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;
import com.tq.common.lambda.utils.Utils;

public class OrderItemDaoimpl extends AbstractItem<OrderItem, String> implements OrderItemDao {

    public OrderItemDaoimpl(AmazonDynamoDB client) {
        super(client, OrderItem.class);
    }

    @Override
    public OrderItem loadItem(String hashKey) {
        if (Utils.isEmpty(hashKey)) return null;
        return super.loadItem(hashKey);
    }
}
