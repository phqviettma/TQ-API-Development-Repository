package com.tq.bridge.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class CFEventUtils {

    public static final Logger log = Logger.getLogger(CFEventUtils.class);

    public static final String API_GATEWAY_STAGES = "prod/";

    public static final String TQ_API_GATEGAY_CF_EVENT = "https://tn1t3stem3.execute-api.us-east-1.amazonaws.com/" + API_GATEWAY_STAGES;

    public static void makeRequest(HttpServletRequest request, HttpServletResponse response, String apiGatewayResource) throws IOException {
        // get Json from incoming request to build new request
        StringBuilder jsonBuff = CFEventUtils.getJsonFromRequest(request);
        // making request to AWS lambda
        Resp awsRes = null;
        try {
            String endpoint = TQ_API_GATEGAY_CF_EVENT + apiGatewayResource;
            awsRes = CFEventUtils.makeAwsLambdaRequest(endpoint, jsonBuff.toString(), request);
        } catch (Exception e) {
            log.error("Error bridge project.", e);
            CFEventUtils.handleErrorResponse(response, e.getMessage());
            return;
        }
        
        CFEventUtils.handleResponse(response, awsRes);
    }

    private static Resp makeAwsLambdaRequest(String endpoint, String json, HttpServletRequest request) throws Exception {
        SSLContext sslcontext = SSLContexts.custom().build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.1", "TLSv1.2" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
            HttpPost postRequest = new HttpPost(endpoint);
            postRequest.addHeader("Content-type", "application/json; charset=UTF-8");
            StringEntity input = new StringEntity(json, "UTF-8");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            return new Resp(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    private static StringBuilder getJsonFromRequest(HttpServletRequest request) {
        StringBuilder jsonBuff = new StringBuilder();
        String reqBody = null;
        try {
            BufferedReader reader = request.getReader();
            while ((reqBody = reader.readLine()) != null)
                jsonBuff.append(reqBody);

        } catch (Exception e) {
            /* error */
            log.error("Brigde Error during reading json request from Click funnel.", e);
        }
        return jsonBuff;
    }

    private static void handleResponse(HttpServletResponse response, Resp res) throws IOException {
    	String awsResJson = res != null ? res.getBody() : null;
        String rebuild = awsResJson == null ? "{\"aws\": \"Response not found.\"}" : awsResJson;
        response.setContentType("application/json");
        response.getWriter().write(rebuild);
        response.setStatus(res.getStatusCode());
    }

    private static void handleErrorResponse(HttpServletResponse response, String error) throws IOException {
        String rebuild = String.format("{\"error\": \"%s\"}", error);
        response.setContentType("application/json");
        response.getWriter().write(rebuild);
        response.setStatus(400);
    }
    
    private static class Resp {
    	private final int statusCode;
    	private final String body;
    	
    	private Resp(int statusCode, String body) {
			this.statusCode = statusCode;
			this.body = body;
		}
		
		public int getStatusCode() {
			return statusCode;
		}
		public String getBody() {
			return body;
		}
    }
}
