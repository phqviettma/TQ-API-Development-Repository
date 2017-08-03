package com.tq.clickfunnel.lambda.handler;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.clickfunnel.lambda.modle.CFContactPayload;
import com.tq.clickfunnel.lambda.modle.CFOrderPayload;

public class JsonRunner {
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        CFContactPayload contact = mapper.readValue(JsonRunner.class.getResourceAsStream("contact-dummy.json"), CFContactPayload.class);
        System.out.println(contact);
        
        CFOrderPayload order = mapper.readValue(JsonRunner.class.getResourceAsStream("order-payload.json"), CFOrderPayload.class);
        System.out.println(order);
    }
}
