package com.tq.clickfunnel.lambda.dynamodb.impl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;

public class ContactItemServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(ContactItemServiceImplTest.class);

    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private ContactItemService m_contactItemService = new ContactItemServiceImpl(m_amazonDynamoDB);

    @Test
    public void testGetContactItem() {
        ContactItem contactItem = m_contactItemService.get("tnnha124@gmail.com");
        log.info("{}", contactItem);
    }
}
