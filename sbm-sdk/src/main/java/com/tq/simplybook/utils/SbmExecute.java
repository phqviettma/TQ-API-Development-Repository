package com.tq.simplybook.utils;

import java.io.Serializable;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tq.simplybook.exception.SbmSDKException;
import com.tq.simplybook.req.SbmReq;
import com.tq.simplybook.resp.SbmErrorResp;

public final class SbmExecute {
    public static <T extends Serializable> String executeWithNoneToken(String endpoint, String method, T object) throws Exception {
        String json = getJsonRequest(method, object);
        return invokeRequest(endpoint, json, new SbmHttpPostReq() {
            @Override
            public HttpPost buildPost(String endpoint) {
                return new HttpPost(endpoint);
            }
        });
    }

    public static <T extends Serializable> String executeWithToken(String companyLogin, String endpoint, String token, String method,
            T object) throws Exception {
        String json = getJsonRequest(method, object);
        System.out.println(json);
        return invokeRequest(endpoint, json, new SbmHttpPostReq() {
            @Override
            public HttpPost buildPost(String endpoint) {
                HttpPost postRequest = new HttpPost(endpoint);
                postRequest.addHeader(SbmHeader.X_COMPANY_LOGIN, companyLogin);
                postRequest.addHeader(SbmHeader.X_TOKEN, token);
                postRequest.addHeader(SbmHeader.CONTENT_TYPE, SbmHeader.APPLICATION_JSON);
                return postRequest;
            }
        });
    }

    public static <T extends Serializable> String executeWithUserToken(String companyLogin, String endpoint, String userToken,
            String method, T object) throws Exception {
        String json = getJsonRequest(method, object);
        System.out.println(json);
        return invokeRequest(endpoint, json, new SbmHttpPostReq() {
            @Override
            public HttpPost buildPost(String endpoint) {
                HttpPost postRequest = new HttpPost(endpoint);
                postRequest.addHeader(SbmHeader.X_COMPANY_LOGIN, companyLogin);
                postRequest.addHeader(SbmHeader.X_ADMIN_TOKEN, userToken);
                postRequest.addHeader(SbmHeader.CONTENT_TYPE, SbmHeader.APPLICATION_JSON);
                return postRequest;
            }
        });
    }

    public static <T extends Serializable> String getJsonRequest(String method, T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(new SbmReq<T>(method, object));
    }

    public static String invokeRequest(String endpoint, String json, SbmHttpPostReq postResq) throws Exception {
        SSLContext sslcontext = SSLContexts.custom().build();
        ;
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.1", "TLSv1.2" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
            HttpPost postRequest = postResq.buildPost(endpoint);
            StringEntity input = new StringEntity(json, "UTF-8");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            throw new SbmSDKException("Error during making request to Simplybook.me", e);
        }
    }
    

    private interface SbmHttpPostReq {
        public HttpPost buildPost(String endpoint);
    }
}
