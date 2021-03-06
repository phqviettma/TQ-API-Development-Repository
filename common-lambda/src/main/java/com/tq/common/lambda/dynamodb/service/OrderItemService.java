package com.tq.common.lambda.dynamodb.service;

import com.tq.common.lambda.dynamodb.model.OrderItem;

public interface OrderItemService extends BaseItemService<OrderItem, Integer> {
    public void delete(OrderItem orderItem);
}
