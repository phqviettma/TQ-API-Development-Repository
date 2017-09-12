package com.tq.cliniko.lambda.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class QueryClinikoApiReq extends GenericClinikoApiReq {
	
	private final String m_queryStatement;
	
	public QueryClinikoApiReq(String apiKey, String httpMethod, String resource, String queryStatement) {
		super(apiKey, httpMethod, resource, null, null);
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
