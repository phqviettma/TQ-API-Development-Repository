package com.tq.simplybook.req;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;
import com.tq.simplybook.resp.ProviderInfo;

public class DeleteSpecialDayReq implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7588650121770515152L;
	
	private Map<String, ProviderInfo> request;

	@JsonValue
	public List<Object> buildArrayParams() {
		List<Object> arrayParams = new ArrayList<Object>();
		arrayParams.add(getRequest().keySet().iterator().next());
		arrayParams.add(getRequest().values().iterator().next());
		return arrayParams;
	}
	
	public Map<String, ProviderInfo> getRequest() {
		return request;
	}

	public void setRequest(Map<String, ProviderInfo> request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return "DeleteSpecialDayReq [request=" + request + "]";
	}
}
