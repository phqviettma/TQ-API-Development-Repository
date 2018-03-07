package com.tq.googlecalendar.lambda.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tq.common.lambda.exception.TrueQuitBadRequest;
import com.tq.googlecalendar.exception.GoogleApiSDKException;
import com.tq.googlecalendar.lambda.model.GoogleConnectFailureResponse;
import com.tq.inf.exception.InfSDKExecption;
import com.tq.simplybook.exception.SbmSDKException;

public class GoogleExceptionHanlder {
	private static ObjectMapper jsonMapper = new ObjectMapper();

	public static GoogleConnectFailureResponse handle(Exception e) {
		GoogleConnectFailureResponse response = new GoogleConnectFailureResponse();
		if (e instanceof SbmSDKException) {
			response.setStatusCode(500);
			response.setErrorMessage(e.getMessage());
		} else if (e instanceof GoogleApiSDKException) {
			response.setStatusCode(500);

		} else if (e instanceof InfSDKExecption) {
			response.setStatusCode(500);

		} else if (e instanceof TrueQuitRegisterException) {
			response.setStatusCode(404);
			response.setErrorMessage(e.getMessage());

		} else if (e instanceof TrueQuitBadRequest) {
			response.setStatusCode(400);
			response.setErrorMessage(e.getMessage());

		}

		response.setSucceeded(false);
		return response;
	}

	public static String buildFailMessageResponse(Boolean isSuccess, String errorMessage) {
		ObjectNode on = jsonMapper.createObjectNode();
		on.put("succeeded", isSuccess);
		on.put("error", errorMessage);
		return on.toString();
	}
}
