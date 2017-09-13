package com.tq.cliniko.lambda.req;

public interface ClinikoApiReq {
	
	String getHttpMethod();
	String getApiKey();
	String getEnpoint();
	String getAccept();
	String getUserAgent();
	String getCotent();
	Object getObject();
}
