package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.ProductItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;

public class ProductItemDaoImpl extends AbstractItem<ProductItem, Integer> implements ProductItemDao {

    public ProductItemDaoImpl(AmazonDynamoDB client) {
        super(client, ProductItem.class);
    }

}
