package com.tq.cliniko.lambda.client;

public interface ClinikoApiReq {
	
	String getHttpMethod();
	String getApiKey();
	String getEnpoint();
	String getAccept();
	String getUserAgent();
	String getCotent();
	Object getObject();
}
