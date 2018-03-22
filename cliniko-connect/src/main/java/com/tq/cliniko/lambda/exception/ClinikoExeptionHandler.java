package com.tq.cliniko.lambda.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tq.cliniko.exception.ClinikoSDKExeption;
import com.tq.cliniko.lambda.resp.ClinikoConnectFailureResponse;
import com.tq.simplybook.exception.SbmSDKException;

public class ClinikoExeptionHandler {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	public static ClinikoConnectFailureResponse handle(Exception e) {
		ClinikoConnectFailureResponse response = new ClinikoConnectFailureResponse();
		if(e instanceof ClinikoSDKExeption) {
			response.setStatusCode(500);
			response.setErrorMessage(e.getMessage());
		}
		else if (e instanceof SbmSDKException) {
			response.setStatusCode(500);
			response.setErrorMessage(e.getMessage());
		}
		else if(e instanceof ClinikoConnectException) {
			response.setStatusCode(404);
			response.setErrorMessage(e.getMessage());
		}
		return response;
		
	}
	public static String buildFailMessageResponse(Boolean isSuccess, String errorMessage) {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", isSuccess);
		on.put("error", errorMessage);
		return on.toString();
	}
	
}
