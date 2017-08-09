package com.tq.common.lambda.dynamodb.impl;

import com.tq.common.lambda.dynamodb.dao.ProductItemDao;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.service.ProductItemService;

public class ProductItemServiceImpl implements ProductItemService {

    private ProductItemDao m_productItemDao;
    
    public ProductItemServiceImpl(ProductItemDao productItemDao) {
        this.m_productItemDao = productItemDao;
    }

    @Override
    public void put(ProductItem productItem) {
        m_productItemDao.putItem(productItem);
    }

    @Override
    public ProductItem load(Integer cfProudctionID) {
        return m_productItemDao.loadItem(cfProudctionID);
    }
}
