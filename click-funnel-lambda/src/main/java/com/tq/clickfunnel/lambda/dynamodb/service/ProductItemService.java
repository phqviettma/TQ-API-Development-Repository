package com.tq.clickfunnel.lambda.dynamodb.service;

import com.tq.clickfunnel.lambda.dynamodb.model.ProductItem;

public interface ProductItemService {
    
    public boolean put(ProductItem productItem);
    
    public ProductItem load(Integer cfProudctionID);
}
