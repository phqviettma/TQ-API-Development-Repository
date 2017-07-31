package com.tq.clickfunnel.lambda.dynamodb.service;

public interface GenericItem <Item, ID>{
    public void putItem(Item item);
    public Item loadItem(Class<Item> item, ID hashKey);
}
