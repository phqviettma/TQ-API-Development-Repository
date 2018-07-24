package com.tq.cliniko.lambda.req;

public class PutClinikoApiReq extends GenericClinikoApiReq {
	public PutClinikoApiReq(String apiKey, String resource, Object object) {
		super( apiKey, "PUT", resource, JSON_MEDIA_TYPE, object);
	}

}
