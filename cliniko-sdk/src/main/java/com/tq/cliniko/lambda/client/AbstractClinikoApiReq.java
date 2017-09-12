package com.tq.cliniko.lambda.client;

public abstract class AbstractClinikoApiReq implements ClinikoApiReq {
	protected final static String JSON_MEDIA_TYPE = "application/json";
	protected final static String USER_AGENT = "TrueQuit";
	protected final static String API_BASE_URL = "https://api.cliniko.com/v1";
	
	private final String m_httpMethod;
	private final String m_apiKey;
	private final String m_content;
	private final Object m_object;	
	private String m_endpoint = null;
	
	public AbstractClinikoApiReq(String apiKey, String httpMethod, String resource, 
			String content, Object object) {
		
		this.m_httpMethod = httpMethod;
		this.m_apiKey = apiKey;
		this.m_content = content;
		this.m_object = object;
		this.m_endpoint = API_BASE_URL + "/" + resource;
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
		return JSON_MEDIA_TYPE;
	}

	@Override
	public String getUserAgent() {
		return USER_AGENT;
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
