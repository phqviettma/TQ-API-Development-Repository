package com.tq.clickfunnel.lambda.dynamodb.service;

import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;

public interface ContactItemService {
    /**
     * Allow save the contact item into DynamoDB
     * {"email": "", client:"{json}"}
     */
    public boolean put(ContactItem contactItem);
    
    /**
     * @param email as hashKey
     * @return contact Item or null
     */
    public ContactItem get(String email);
}
