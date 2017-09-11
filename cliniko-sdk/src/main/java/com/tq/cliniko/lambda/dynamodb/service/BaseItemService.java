package com.tq.cliniko.lambda.dynamodb.service;

public interface BaseItemService<Item, ID> {

    public void put(Item item);
    
    public Item load(ID key);
}
