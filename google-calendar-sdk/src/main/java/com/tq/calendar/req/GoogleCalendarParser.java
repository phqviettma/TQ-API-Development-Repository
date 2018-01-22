package com.tq.calendar.req;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.calendar.exception.GoogleApiSDKException;
import com.tq.calendar.exception.GoogleResponseValidator;
import com.tq.calendar.resp.GoogleError;
import com.tq.calendar.resp.GoogleErrorResp;

public class GoogleCalendarParser {
	private static ObjectMapper JSON_MAPPER = new ObjectMapper();
	private static final Logger log = Logger.getLogger(GoogleCalendarParser.class);
	public static <T extends Serializable> T readJsonValueForObject(String jsonResp, Class<T> classes) throws GoogleApiSDKException {
		T object = null;
		try {
			object = JSON_MAPPER.readValue(jsonResp, classes);
			if (GoogleResponseValidator.failed(object)) {
				throw new GoogleApiSDKException("Error during deserialize response " + String.valueOf(jsonResp) + " with class " + classes.getName());
			}
		} catch (IOException e) {
			handleParseJsonError(jsonResp, e);
		}
		return object;
	}

	private static void handleParseJsonError(String jsonResp, IOException e) throws GoogleApiSDKException {
		try {
			GoogleErrorResp resp = JSON_MAPPER.readValue(jsonResp, GoogleErrorResp.class);
			GoogleError error = resp.getError();
			throw new GoogleApiSDKException(error.getCode()+":"+error.getMessage() ,e);
		} catch (Exception e2) {
			 log.error(e2.getMessage());
	            throw new GoogleApiSDKException("Error during reading response error : " + jsonResp, e2);
		}
	}
}
