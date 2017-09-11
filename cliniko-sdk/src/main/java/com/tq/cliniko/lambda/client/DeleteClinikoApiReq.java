package com.tq.cliniko.lambda.client;

public class DeleteClinikoApiReq extends GenericClinikoApiReq {
	public DeleteClinikoApiReq(String baseApiUrl,String apiKey, String resource) {
		super(baseApiUrl,apiKey, "DELETE", resource, JSON_MEDIA_TYPE, null);
	}

}
