package com.tq.clickfunnel.lambda.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;

public interface CFLambdaServiceRepository {
    
    public AmazonDynamoDB getAmazonDynamoDB();
    
    public ContactItemService getContactItemService();
}
