package com.tq.inf.service;

import com.tq.inf.model.ProxyConfig;

public interface ApiContext {
	
	public String getApiKey();

	public String getApiName();

	public ProxyConfig getProxyConfig();
}
