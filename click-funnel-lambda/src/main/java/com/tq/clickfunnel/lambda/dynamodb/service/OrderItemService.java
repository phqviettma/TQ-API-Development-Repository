package com.tq.clickfunnel.lambda.dynamodb.service;

import com.tq.clickfunnel.lambda.dynamodb.model.OrderItem;

public interface OrderItemService {
    
    public boolean put(OrderItem orderItem);
    
    public OrderItem load(String email);
}
