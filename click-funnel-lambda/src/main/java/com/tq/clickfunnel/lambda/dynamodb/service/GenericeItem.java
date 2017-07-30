package com.tq.clickfunnel.lambda.dynamodb.service;

public interface GenericeItem <Item, ID>{
    public void putItem(Item item);
}
