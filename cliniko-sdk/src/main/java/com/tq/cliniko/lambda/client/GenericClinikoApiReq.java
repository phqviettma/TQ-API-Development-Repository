package com.tq.cliniko.lambda.client;

public class GenericClinikoApiReq extends AbstractClinikoApiReq {	
	public GenericClinikoApiReq(String baseApiUrl, String apiKey, String httpMethod, String resource, String content, Object object) {
		super(baseApiUrl, apiKey, httpMethod, resource, JSON_MEDIA_TYPE, USER_AGENT,content, object);
	}
}
