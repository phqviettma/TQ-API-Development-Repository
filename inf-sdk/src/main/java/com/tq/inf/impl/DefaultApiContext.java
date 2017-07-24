package com.tq.inf.impl;

import com.tq.inf.model.ProxyConfig;
import com.tq.inf.service.ApiContext;

public class DefaultApiContext implements ApiContext {
    private static ProxyConfig proxyConfig = null;
    static {
        loadProxyConfig();
    }

    private String m_apiName;

    private String m_apiKey;

    public DefaultApiContext(String apiUrl, String apiKey) {
        this.m_apiName = apiUrl;
        this.m_apiKey = apiKey;
    }

    @Override
    public String getApiKey() {
        return m_apiKey;
    }

    @Override
    public String getApiName() {
        return m_apiName;
    }

    @Override
    public String toString() {
        return String.format("[API NAME : %s , API KEY : %s, Paramerters : %s ] ", m_apiName, m_apiKey);
    }

    @Override
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    private static void loadProxyConfig() {
        String proxyhost = System.getProperty(ProxyConfig.PROXY_HOST_KEY);
        String proxyPort = System.getProperty(ProxyConfig.PROXY_PORT_KEY);
        if (proxyhost != null && !proxyhost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
            proxyConfig = new ProxyConfig(proxyhost, proxyPort);
        }
    }
}
