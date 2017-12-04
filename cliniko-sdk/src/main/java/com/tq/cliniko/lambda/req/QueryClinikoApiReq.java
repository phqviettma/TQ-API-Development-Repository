package com.tq.cliniko.lambda.req;

public abstract class QueryClinikoApiReq extends GenericClinikoApiReq {
	
	private final String m_queryStatement;
	
	public QueryClinikoApiReq(String apiKey, String resource, String queryStatement) {
		super(apiKey, "GET", resource, null, null);
		m_queryStatement = queryStatement;
	}
	
	@Override
	public String getEnpoint() {
		if(m_queryStatement != null) {
			return super.getEnpoint() + m_queryStatement;
		} else {
			return super.getEnpoint();
		}
	}
}
