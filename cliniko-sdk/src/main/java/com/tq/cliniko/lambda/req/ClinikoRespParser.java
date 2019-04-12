package com.tq.cliniko.lambda.req;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClinikoRespParser {
	
	private static ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	public static <T> T readJsonValueForObject(String jsonResp, Class<T> classes){
		T object = null;
        try {
        	object = JSON_MAPPER.readValue(jsonResp, classes);
        } catch (IOException e) {
            handleParseJsonError(jsonResp, e);
        }
        return object;
	}

	private static void handleParseJsonError(String jsonResp, IOException e) {
		
	}
	
	public static ObjectMapper getObjectMapper() {
		return JSON_MAPPER;
	}
	
}
