package com.tq.clickfunnel.lambda.dynamodb.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;
import com.tq.clickfunnel.lambda.dynamodb.service.AbstractItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ProductItemService;

public class ProductItemServiceImpl extends AbstractItem<ProductItem, Integer> implements ProductItemService {

    public ProductItemServiceImpl(AmazonDynamoDB client) {
        super(client, ProductItem.class);
    }

    @Override
    public boolean put(ProductItem productItem) {
        putItem(productItem);
        return true;
    }

    @Override
    public ProductItem load(Integer cfProudctionID) {
        return loadItem(cfProudctionID);
    }

}
