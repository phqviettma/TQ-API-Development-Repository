package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.OrderItemDao;
import com.tq.common.lambda.dynamodb.model.OrderItem;
import com.tq.common.lambda.dynamodb.service.OrderItemService;

public class OrderItemServiceImpl implements OrderItemService {
    
    private OrderItemDao m_orderItemDao;
    
    public OrderItemServiceImpl(OrderItemDao orderItemDao) {
        m_orderItemDao = orderItemDao;
    }

    @Override
    public void put(OrderItem orderItem) {
        m_orderItemDao.putItem(orderItem);
    }

    @Override
    public OrderItem load(Integer pruchaseId) {
        return m_orderItemDao.loadItem(pruchaseId);
    }

    @Override
    public void delete(OrderItem orderItem) {
        m_orderItemDao.deleteItem(orderItem);
    }



}
