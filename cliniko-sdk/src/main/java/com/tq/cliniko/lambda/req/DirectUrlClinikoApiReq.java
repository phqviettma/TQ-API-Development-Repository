package com.tq.cliniko.lambda.req;

public class DirectUrlClinikoApiReq implements ClinikoApiReq {
	
	private final String url;
	private final String method;
	private final String apiKey;
	
	public DirectUrlClinikoApiReq(String apiKey, String method, String url) {
		this.apiKey = apiKey;
		this.url = url;
		this.method = method;
	}
	
	@Override
	public String getEnpoint() {
		return this.url;
	}

	@Override
	public String getHttpMethod() {
		return this.method;
	}

	@Override
	public String getApiKey() {
		return this.apiKey;
	}

	@Override
	public String getAccept() {
		return JSON_MEDIA_TYPE;
	}

	@Override
	public String getUserAgent() {
		return USER_AGENT;
	}

	@Override
	public String getCotent() {
		return JSON_MEDIA_TYPE;
	}

	@Override
	public Object getObject() {
		return null;
	} 
}
