package com.tq.clickfunnel.lambda.dynamodb.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.CountryItem;
import com.tq.clickfunnel.lambda.dynamodb.service.AbstractItem;
import com.tq.clickfunnel.lambda.dynamodb.service.CountryItemService;

public class CountryItemServiceImpl extends AbstractItem<CountryItem, String> implements CountryItemService {

    public CountryItemServiceImpl(AmazonDynamoDB client) {
        super(client, CountryItem.class);
    }

    @Override
    public boolean put(CountryItem countryItem) {
        putItem(countryItem);
        return true;
    }

    @Override
    public CountryItem load(String name) {
        //Error 'no HASH key value present'
        if (name == null || name.isEmpty()) return null;
        return loadItem(name);
    }
}
