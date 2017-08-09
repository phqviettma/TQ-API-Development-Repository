package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.ContactItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;
import com.tq.common.lambda.utils.Utils;

public class ContactItemDaoImpl extends AbstractItem<ContactItem, String> implements ContactItemDao {

    public ContactItemDaoImpl(AmazonDynamoDB client) {
        super(client, ContactItem.class);
    }
    
    @Override
    public ContactItem loadItem(String email) {
        //Error 'no HASH key value present'
        if (Utils.isEmpty(email)) return null;
        return super.loadItem(email);
    }
}
