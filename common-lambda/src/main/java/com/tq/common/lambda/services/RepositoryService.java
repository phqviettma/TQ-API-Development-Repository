package com.tq.common.lambda.services;

import com.tq.common.lambda.dynamodb.service.ContactItemService;
import com.tq.common.lambda.dynamodb.service.CountryItemService;
import com.tq.common.lambda.dynamodb.service.OrderItemService;
import com.tq.common.lambda.dynamodb.service.ProductItemService;
import com.tq.common.lambda.dynamodb.service.SignupItemService;

public interface RepositoryService {
    
    ContactItemService getContactItemService();
    
    SignupItemService getSignupItemService();
    
    CountryItemService getCountryItemService();
    
    OrderItemService getOrderItemService();
    
    ProductItemService getProductItemService();
}
