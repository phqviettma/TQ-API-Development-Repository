package com.tq.cliniko.lambda.req;

public interface ClinikoApiReq {
	final static String JSON_MEDIA_TYPE = "application/json";
	final static String USER_AGENT = "TrueQuit";
	final static String API_BASE_URL = "https://api.cliniko.com/v1";
	
	String getHttpMethod();
	String getApiKey();
	String getEnpoint();
	String getAccept();
	String getUserAgent();
	String getCotent();
	Object getObject();
}
