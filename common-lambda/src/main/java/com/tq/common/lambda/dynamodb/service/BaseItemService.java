package com.tq.common.lambda.dynamodb.service;

public interface BaseItemService<Item, ID> {

    public void put(Item item);
    
    public Item load(ID key);
}
