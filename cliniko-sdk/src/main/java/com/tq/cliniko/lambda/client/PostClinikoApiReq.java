package com.tq.cliniko.lambda.client;

public class PostClinikoApiReq extends GenericClinikoApiReq {
	public PostClinikoApiReq(String apiKey, String resource, Object object) {
		super( apiKey, "POST", resource, JSON_MEDIA_TYPE, object);
	}

}
