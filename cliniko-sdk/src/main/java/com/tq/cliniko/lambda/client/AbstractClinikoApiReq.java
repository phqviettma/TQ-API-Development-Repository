package com.tq.cliniko.lambda.client;

public abstract class AbstractClinikoApiReq implements ClinikoApiReq {
	protected final static String JSON_MEDIA_TYPE = "application/json";
	protected final static String USER_AGENT = "TrueQuit";
	
	private final String m_httpMethod;
	private final String m_baseApiUrl;
	private final String m_apiKey;
	private final String m_accept;
	private final String m_userAgent;
	private final String m_content;
	private final Object m_object;	
	private String m_endpoint = null;
	
	public AbstractClinikoApiReq(String baseApiUrl, String apiKey, String httpMethod, String resource, String accept, String userAgent, String content, Object object) {
		this.m_httpMethod = httpMethod;
		this.m_baseApiUrl = baseApiUrl;
		this.m_apiKey = apiKey;
		this.m_accept = accept;
		this.m_userAgent = userAgent;
		this.m_content = content;
		this.m_object = object;
		
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
		return m_content;
	}

	@Override
	public Object getObject() {
		return m_object;
	}
	

}
