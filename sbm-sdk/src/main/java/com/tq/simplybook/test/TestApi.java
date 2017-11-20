package com.tq.simplybook.test;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.tq.simplybook.exception.SbmSDKException;

public abstract class TestApi {

	public static void main(String[] args) throws Exception {
		System.out.println(invokeGetRequest("https://api.cliniko.com/v1/appointments", "b66b4caab8e4fd3e03b1dc88aa064339"));
	}
	
	public static String invokeGetRequest(String endpoint, String apiKey) throws Exception {
        SSLContext sslcontext = SSLContexts.custom().build();
        ;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.1", "TLSv1.2" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
            HttpGet getRequest = new HttpGet(endpoint);
			
			String userPassword = apiKey + ":" + "";
	        String encodedString = Base64.encodeBase64String(userPassword.getBytes());
			
			getRequest.addHeader("Accept", "application/json");
			getRequest.addHeader("User-Agent", "APP_VENDOR_NAME (APP_VENDOR_EMAIL)");
			getRequest.addHeader("Authorization", "Basic " + encodedString);
			
            HttpResponse response = httpClient.execute(getRequest);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new SbmSDKException(e.getMessage(), e);
        }
    }

}
