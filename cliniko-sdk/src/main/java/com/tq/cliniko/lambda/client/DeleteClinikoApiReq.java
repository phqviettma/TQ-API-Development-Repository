package com.tq.cliniko.lambda.client;

public class DeleteClinikoApiReq extends GenericClinikoApiReq {
	public DeleteClinikoApiReq(Env env, String resource) {
		super(env.getApiUrl(),env.getApiKey(), "DELETE", resource, JSON_MEDIA_TYPE, null);
	}

}
