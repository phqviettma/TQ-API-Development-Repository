package com.tq.inf.model;

public class ProxyConfig {
    public static final String PROXY_HOST_KEY = "proxyHost";
    public static final String PROXY_PORT_KEY = "proxyPort";

    private String proxyHost = null;
    private String proxyPort = "";

    public ProxyConfig(String proxyHost, String proxyPort) {
        this.setProxyHost(proxyHost);
        this.setProxyPort(proxyPort);
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public String toString() {
        return "ProxyConfig [" + proxyHost + ":" + proxyPort + "]";
    }
}
