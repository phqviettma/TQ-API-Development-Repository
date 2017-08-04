package com.tq.clickfunnel.lambda.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.OrderItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.ProductItemService;

public interface CFLambdaServiceRepository {
    
    public AmazonDynamoDB getAmazonDynamoDB();
    
    public ContactItemService getContactItemService();
    
    public ProductItemService getProductItemService();
    
    public OrderItemService getOrderItemService();
}
