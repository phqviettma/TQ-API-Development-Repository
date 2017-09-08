package com.tq.cliniko.lambda.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class QueryClinikoApiReq extends GenericClinikoApiReq {
	
	private final String m_queyStatement;
	
	public QueryClinikoApiReq(Env env, String httpMethod, String resource, String cotent, String queyStatement, String json) {
		super(env, httpMethod, resource, cotent, json);
		m_queyStatement = queyStatement;
	}
	
	@Override
	public String getEnpoint() {
		if(m_queyStatement != null) {
			try {
				return super.getEnpoint() + "?q[]=" + URLEncoder.encode(m_queyStatement, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
			return super.getEnpoint();
		}
	}
}
