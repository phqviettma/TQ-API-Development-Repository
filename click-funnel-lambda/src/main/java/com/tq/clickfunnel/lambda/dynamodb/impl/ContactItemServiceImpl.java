package com.tq.clickfunnel.lambda.dynamodb.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.AbstractItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;

public class ContactItemServiceImpl extends AbstractItem<ContactItem, String> implements ContactItemService {

    public ContactItemServiceImpl(AmazonDynamoDB client) {
        super(client, ContactItem.class);
    }

    @Override
    public boolean put(ContactItem contactItem) {
        putItem(contactItem);
        return true;
    }

    @Override
    public ContactItem get(String email) {
        //Error 'no HASH key value present'
        if (email == null || email.isEmpty())
            return null;
        return loadItem(getItem(), email);
    }

}
