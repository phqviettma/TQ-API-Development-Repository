package com.tq.clickfunnel.lambda.impl;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.impl.ContactItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.impl.CountryItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.impl.OrderItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.impl.ProductItemServiceImpl;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.CountryItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.OrderItemService;
import com.tq.clickfunnel.lambda.dynamodb.service.ProductItemService;
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
    @Override
    public ProductItemService getProductItemService() {
        return new ProductItemServiceImpl(m_amazonDynamoDB);
    }
    @Override
    public OrderItemService getOrderItemService() {
        return new OrderItemServiceImpl(m_amazonDynamoDB);
    }
    @Override
    public CountryItemService getCountryItemService() {
        return new CountryItemServiceImpl(m_amazonDynamoDB);
    }

}
