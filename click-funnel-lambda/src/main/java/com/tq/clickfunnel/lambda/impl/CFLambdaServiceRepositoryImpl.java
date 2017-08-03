package com.tq.clickfunnel.lambda.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.service.CFLambdaServiceRepository;

public class CFLambdaServiceRepositoryImpl implements CFLambdaServiceRepository {
    private AmazonDynamoDB m_amazonDynamoDB;
    
    public CFLambdaServiceRepositoryImpl(AmazonDynamoDB amazonDynamoDB) {
        this.m_amazonDynamoDB = amazonDynamoDB;
    }
    @Override
    public ContactItemService getContactItemService() {
        return new ContactItemServiceImpl(m_amazonDynamoDB);
    }
    @Override
    public AmazonDynamoDB getAmazonDynamoDB() {
        return m_amazonDynamoDB;
    }

}
