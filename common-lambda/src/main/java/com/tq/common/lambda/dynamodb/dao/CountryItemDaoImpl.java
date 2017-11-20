package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.CountryItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;
import com.tq.common.lambda.utils.Utils;

public class CountryItemDaoImpl extends AbstractItem<CountryItem, String> implements CountryItemDao {

    public CountryItemDaoImpl(AmazonDynamoDB client) {
        super(client, CountryItem.class);
    }

    @Override
    public CountryItem loadItem(String countryName) {
        if (Utils.isEmpty(countryName)) return null;
        return super.loadItem(countryName);
    }

    
}
