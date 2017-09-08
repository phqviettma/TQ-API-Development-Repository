package com.tq.cliniko.lambda.client;

public class GenericClinikoApiReq extends AbstractClinikoApiReq {
	private static final String ACCEPT_MEDIA_TYPE = "application/json";
	private static final String USER_AGENT = "TrueQuit";

	public GenericClinikoApiReq(Env env, String httpMethod, String resource, String cotent, String json) {
		super(httpMethod, env.getApiUrl(), env.getApiKey(), resource, ACCEPT_MEDIA_TYPE, USER_AGENT, cotent, json);
	}

}
