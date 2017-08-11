package com.tq.inf.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;

import com.tq.inf.exception.InfSDKExecption;
import com.tq.inf.impl.DefaultApiContext;
import com.tq.inf.model.ProxyConfig;
import com.tq.inf.service.ActionCallback;
import com.tq.inf.service.ApiContext;

public final class XmlRqcUtils {
    private static final Logger log = Logger.getLogger(XmlRqcUtils.class);
    
    public static Object execute(String apiUrl, String apiKey, ActionCallback action, final String methodName) throws InfSDKExecption {
        ApiContext apiContext = new DefaultApiContext(apiUrl, apiKey);
        return execute(apiContext, action, methodName);
    }
    
    public static Object execute(ApiContext apiContext, ActionCallback action, final String methodName) throws InfSDKExecption {
        XmlRpcClient xmlRpcClient = null;
        try {
            long start = System.currentTimeMillis();
            xmlRpcClient = getXmlRqc(apiContext.getApiName());
            ProxyConfig proxyConfig = apiContext.getProxyConfig();
            if (proxyConfig != null) {
                log.info("Execute XML-RCP via " + proxyConfig);
                loadProxy(xmlRpcClient, proxyConfig);
            }
            List<?> prams = action.getParameters(apiContext);
            if (log.isDebugEnabled()) {
                log.debug(prams);
            }
            Object result = xmlRpcClient.execute(methodName, prams);
            log.info(String.format("Took %d to execute %s .", System.currentTimeMillis() - start, System.currentTimeMillis() - start));
            return result;
        } catch (MalformedURLException | XmlRpcException e) {
            throw new InfSDKExecption(e.getMessage(), e);
        }
    }

    private static void loadProxy(XmlRpcClient xmlRpcClient, ProxyConfig proxyConfig) {
        XmlRpcSun15HttpTransportFactory pFactory = new XmlRpcSun15HttpTransportFactory(xmlRpcClient);
        pFactory.setProxy(proxyConfig.getProxyHost(), Integer.valueOf(proxyConfig.getProxyPort()));
        xmlRpcClient.setTransportFactory(pFactory);
    }

    public static XmlRpcClient getXmlRqc(String apiUrl) throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(apiUrl));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        return client;
    }
}
