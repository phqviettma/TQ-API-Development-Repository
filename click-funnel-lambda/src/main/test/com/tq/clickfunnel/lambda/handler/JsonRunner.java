package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.modle.ContactPayload;

public class JsonRunner {
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ContactPayload contact = mapper.readValue(JsonRunner.class.getResourceAsStream("contact-dummy.json"), ContactPayload.class);
        System.out.println(contact);
    }
}
