package com.tq.cliniko.lambda.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class QueryClinikoApiReq extends GenericClinikoApiReq {
	
	private final String m_queryStatement;
	
	public QueryClinikoApiReq(String baseApiUrl, String apiKey, String httpMethod, String resource, String content, String queryStatement, String json) {
		super(baseApiUrl,apiKey, httpMethod, resource, content, json);
		m_queryStatement = queryStatement;
	}
	
	@Override
	public String getEnpoint() {
		if(m_queryStatement != null) {
			try {
				return super.getEnpoint() + "?q[]=" + URLEncoder.encode(m_queryStatement, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
			return super.getEnpoint();
		}
	}
}
