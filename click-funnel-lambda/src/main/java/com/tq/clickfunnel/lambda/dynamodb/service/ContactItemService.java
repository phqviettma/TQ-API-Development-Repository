package com.tq.clickfunnel.lambda.dynamodb.service;

import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;

public interface ContactItemService {
    public void put(ContactItem contactItem);
}
