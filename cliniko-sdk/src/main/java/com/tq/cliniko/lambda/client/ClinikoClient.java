package com.tq.cliniko.lambda.client;

import java.net.URI;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.cliniko.exception.ClinikoSDKExeption;

public class ClinikoClient {
	private static final ObjectMapper m_mapper = new ObjectMapper();

	public static String request(ClinikoApiReq apiReq) throws Exception {
		HttpRequestBase req = null;

		switch (apiReq.getHttpMethod()) {
		case "GET":
			req = new HttpGet();
			break;
		case "DELETE":
			req = new HttpDelete();
			break;
		case "POST":
			req = new HttpPost();
			break;
		}

		req.setURI(URI.create(apiReq.getEnpoint()));

		String userPassword = apiReq.getApiKey() + ":" + "";
		String encodedString = Base64.encodeBase64String(userPassword.getBytes());

		req.setHeader("Authorization", "Basic " + encodedString);

		if (apiReq.getAccept() != null) {
			req.setHeader("Accept", apiReq.getAccept());
		}

		if (apiReq.getCotent() != null) {
			req.setHeader("Content-Type", apiReq.getAccept());
		}

		if (req instanceof HttpEntityEnclosingRequestBase) {
			if (apiReq.getObject() != null) {
				String json = m_mapper.writeValueAsString(apiReq.getObject());
				((HttpEntityEnclosingRequestBase) req).setEntity(new StringEntity(json, "UTF-8"));
			}
		}

		return request(req);
	}

	private static String request(HttpRequestBase req) throws Exception {
		SSLContext sslcontext = SSLContexts.custom().build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
				new String[] { "TLSv1.1", "TLSv1.2" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
			System.out.println(req);
			HttpResponse response = httpClient.execute(req);
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			throw new ClinikoSDKExeption(e.getMessage(), e);
		}
	}
}
