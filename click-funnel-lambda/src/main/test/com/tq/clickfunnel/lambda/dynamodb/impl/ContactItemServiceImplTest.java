package com.tq.clickfunnel.lambda.dynamodb.impl;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.dynamodb.model.ContactItem;
import com.tq.clickfunnel.lambda.dynamodb.service.ContactItemService;
import com.tq.clickfunnel.lambda.handler.JsonRunner;
import com.tq.clickfunnel.lambda.utils.DynanodbUtils;
import com.tq.clickfunnel.lambda.utils.JsonUtils;

public class ContactItemServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(ContactItemServiceImplTest.class);

    AmazonDynamoDB m_amazonDynamoDB = DynanodbUtils.getLocallyDynamoDB(); // Verify local or mock
    private ContactItemService m_contactItemService = new ContactItemServiceImpl(m_amazonDynamoDB);

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetContactItem() {
        ContactItem contactItem = m_contactItemService.get("tnnha124@gmail.com");
        log.info("{}", contactItem);
    }

    @Test
    public void testCreateDummyContactFromFile() throws JsonParseException, JsonMappingException, IOException {
        String jsonString = JsonUtils.getJsonString(JsonRunner.class.getResourceAsStream("contactItem-dummy-uf238.json"));
        ContactItem contactDummy = mapper.readValue(jsonString, ContactItem.class);
        m_contactItemService.put(contactDummy);
    }
}
