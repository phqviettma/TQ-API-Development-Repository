package com.tq.cliniko.lambda.client;

public class GenericClinikoApiReq extends AbstractClinikoApiReq {	
	public GenericClinikoApiReq(String apiKey, String httpMethod, String resource, String content, Object object) {
		super(apiKey, httpMethod, resource, content, object);
	}
}
