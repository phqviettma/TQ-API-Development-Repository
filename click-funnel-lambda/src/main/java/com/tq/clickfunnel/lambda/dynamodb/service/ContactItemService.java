package com.tq.clickfunnel.lambda.dynamodb.service;

import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;

public interface ContactItemService {
    /**
     * Allow save the contact item into DynamoDB
     * {"email": "", client:"{json}"}
     */
    public void put(ContactItem contactItem);
    
    /**
     * 
     * @return the contact item or null
     */
    public ContactItem getContactItem(String email);
}
