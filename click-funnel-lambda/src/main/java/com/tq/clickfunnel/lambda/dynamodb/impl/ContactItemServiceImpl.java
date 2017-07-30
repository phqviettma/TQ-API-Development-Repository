package com.tq.clickfunnel.lambda.dynamodb.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.AbstractItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;

public class ContactItemServiceImpl extends AbstractItem<ContactItem, Integer> implements ContactItemService {

    public ContactItemServiceImpl(AmazonDynamoDB client) {
        super(client, ContactItem.class);
    }

    @Override
    public void put(ContactItem contactItem) {
        putItem(contactItem);
    }

}
