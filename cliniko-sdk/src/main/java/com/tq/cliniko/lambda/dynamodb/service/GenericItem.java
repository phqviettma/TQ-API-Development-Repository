package com.tq.cliniko.lambda.dynamodb.service;

public interface GenericItem <Item, ID>{
    public void putItem(Item item);
    public Item loadItem(ID hashKey);
    public void deleteItem(Item item);
}
