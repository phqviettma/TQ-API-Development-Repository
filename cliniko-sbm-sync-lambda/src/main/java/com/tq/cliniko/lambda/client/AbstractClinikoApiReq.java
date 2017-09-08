package com.tq.cliniko.lambda.client;

public abstract class AbstractClinikoApiReq implements ClinikoApiReq {
	private final String m_httpMethod;
	private final String m_baseApiUrl;
	private final String m_apiKey;
	private final String m_accept;
	private final String m_userAgent;
	private final String m_cotent;
	private final String m_object;	
	private String m_endpoint = null;
	
	public AbstractClinikoApiReq(String httpMethod, String baseApiUrl, String apiKey, String resource, String accept, String userAgent, String cotent, String json) {
		this.m_httpMethod = httpMethod;
		this.m_baseApiUrl = baseApiUrl;
		this.m_apiKey = apiKey;
		this.m_accept = accept;
		this.m_userAgent = userAgent;
		this.m_cotent = cotent;
		this.m_object = json;
		
		this.m_endpoint = m_baseApiUrl + "/" + resource;
	}
	
	@Override
	public String getHttpMethod() {
		return m_httpMethod;
	}
	
	@Override
	public String getEnpoint() {
		return this.m_endpoint;
	}
	
	@Override
	public String getApiKey() {
		return m_apiKey;
	}

	@Override
	public String getAccept() {
		return m_accept;
	}

	@Override
	public String getUserAgent() {
		return m_userAgent;
	}

	@Override
	public String getCotent() {
		return m_cotent;
	}

	@Override
	public String getObject() {
		return m_object;
	}
	

}
