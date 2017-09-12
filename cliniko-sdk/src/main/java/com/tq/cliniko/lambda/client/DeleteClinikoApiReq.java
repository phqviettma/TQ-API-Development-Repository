package com.tq.cliniko.lambda.client;

public class DeleteClinikoApiReq extends GenericClinikoApiReq {
	public DeleteClinikoApiReq(String apiKey, String resource) {
		super(apiKey, "DELETE", resource, JSON_MEDIA_TYPE, null);
	}
}
