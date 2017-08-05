package com.tq.clickfunnel.lambda.dynamodb.service;

import com.tq.clickfunnel.lambda.dynamodb.model.CountryItem;

public interface CountryItemService {
    
    public boolean put(CountryItem countryItem);
    
    public CountryItem load(String name);
    
}
