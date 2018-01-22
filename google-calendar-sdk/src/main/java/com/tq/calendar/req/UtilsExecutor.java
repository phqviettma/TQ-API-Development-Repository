package com.tq.calendar.req;

import java.net.URI;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.calendar.exception.GoogleApiSDKException;

public class UtilsExecutor {
	private static final ObjectMapper m_mapper = new ObjectMapper();
	private static final Logger m_log = LoggerFactory.getLogger(UtilsExecutor.class);

	public static String request(GoogleCalendarReq apiReq) throws Exception {
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
		req.setURI(URI.create(apiReq.getEndPoint()));
		req.setHeader("Authorization", "Bearer " + apiReq.getAccessToken());
		if (apiReq.getContentType() != null) {
			req.setHeader("Content-Type", apiReq.getContentType());
		}
		if (req instanceof HttpEntityEnclosingRequestBase) {
			if (apiReq.getObject() != null) {
				String json = m_mapper.writeValueAsString(apiReq.getObject());
				((HttpEntityEnclosingRequestBase) req).setEntity(new StringEntity(json, "UTF-8"));
			}
		}
		return request(req);
	}

	public static String request(HttpRequestBase req) throws Exception {
		SSLContext sslcontext = SSLContexts.custom().build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
				new String[] { "TLSv1.1", "TLSv1.2" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
			m_log.info("Req" + req);

			if (req instanceof HttpEntityEnclosingRequestBase) {
				HttpEntity entity = ((HttpEntityEnclosingRequestBase) req).getEntity();
				if (entity != null) {
					String body = EntityUtils.toString(entity, "UTF-8");
					m_log.info("Body" + body);
				}
			}

			HttpResponse response = httpClient.execute(req);
			return response.getEntity() == null ? null : EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			throw new GoogleApiSDKException(e.getMessage(), e);
		}
	}
}
