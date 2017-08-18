package com.tq.common.lambda.dynamodb.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.common.lambda.dynamodb.model.SignupItem;
import com.tq.common.lambda.dynamodb.service.AbstractItem;
import com.tq.common.lambda.utils.Utils;

public class SignupItemDaoImpl extends AbstractItem<SignupItem, String> implements SignupItemDao {

    public SignupItemDaoImpl(AmazonDynamoDB client) {
        super(client, SignupItem.class);
    }
    
    @Override
    public SignupItem loadItem(String email) {
        //Error 'no HASH key value present'
        if (Utils.isEmpty(email)) return null;
        return super.loadItem(email);
    }
}

