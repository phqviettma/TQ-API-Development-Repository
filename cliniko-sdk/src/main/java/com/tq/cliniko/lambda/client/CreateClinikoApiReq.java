package com.tq.cliniko.lambda.client;

public class CreateClinikoApiReq extends GenericClinikoApiReq {
	public CreateClinikoApiReq(String baseApiUrl, String apiKey, String resource, Object object) {
		super(baseApiUrl, apiKey, "POST", resource, JSON_MEDIA_TYPE, object);
	}

}
