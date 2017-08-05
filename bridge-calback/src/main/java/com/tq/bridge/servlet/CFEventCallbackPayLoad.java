package com.tq.bridge.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class CFCallbackPayLoad
 * configuration callback URL : 
 * http://elasticbeanstalk-env.com/clickfunnel?event=
 * 
 * Here, elasticbeanstalk-env URK : 
 * when you deploy an application on Elastic Beanstalk, it will automacally generate the URL associated with environment
 * So, assumed the elasticbeanstalk-env is URL
 */
@WebServlet("/clickfunnel")
public class CFEventCallbackPayLoad extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(CFEventCallbackPayLoad.class);
    
    public static final String CONTACT_EVENT_CREATED = "contact?event=contact-created";
    public static final String ORDER_EVENT_CREATED = "order?event=order-created";
    public static final String ORDER_EVENT_UPDATED = "order?event=order-updated";    
    public static final String API_GATEWAY_STAGES = "prod/";

    public static final String TQ_API_GATEGAY_CF_EVENT = "https://yhy9f83hp2.execute-api.us-east-1.amazonaws.com/" + API_GATEWAY_STAGES;
    public static final Map<String, String > mapEventForward = new HashMap<>();
    static {
        mapEventForward.put("contact-created", CONTACT_EVENT_CREATED);
        mapEventForward.put("order-created", ORDER_EVENT_CREATED);
        mapEventForward.put("order-created", ORDER_EVENT_UPDATED);
    }

    /**
     * Default constructor.
     */
    public CFEventCallbackPayLoad() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Handle error if having
        String event = request.getParameter("event");
        String handleValidationErrorEvent = handleValidationErrorEvent(event);
        if (handleValidationErrorEvent != null ) {
            handleErrorResponse(response, handleValidationErrorEvent);
            return;
        }
        // get Json from incoming request to build new request
        StringBuilder jsonBuff = getJsonFromRequest(request);
        // making request to AWS lambda
        String awsRes = null;
        try {
            String endpoint = TQ_API_GATEGAY_CF_EVENT + mapEventForward.get(event);
            awsRes = makeAwsLambdaRequest(endpoint , jsonBuff.toString(), request);
        } catch (Exception e) {
            log.error("error bridge project.", e);
            handleErrorResponse(response, e.getMessage());
            return;
        }
        handleResponse(response, awsRes);
    }

    private StringBuilder getJsonFromRequest(HttpServletRequest request) {
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

    private String handleValidationErrorEvent(String event) throws IOException {
        log.info("Click Funnel Event:{}", event);
        if (event == null || event.isEmpty()) {
            return "Event (event) parameter is not input URL.";
        }
        if (mapEventForward.get(event) == null) {
            String error = String.format("Event (event = {}) parameter is not supported now. Please check for callback URL.", event);
            return error;
        }
        return null;
    }

    private void handleResponse(HttpServletResponse response, String awsResJson) throws IOException {
        String rebuild = awsResJson == null ? "{\"aws\": \"Response not found.\"}" : awsResJson;
        response.setContentType("application/json");
        response.getWriter().write(rebuild);
    }
    
    private void handleErrorResponse(HttpServletResponse response, String error) throws IOException {
        String rebuild =  String.format("{\"error\": \"%s\"}", error);
        response.setContentType("application/json");
        response.getWriter().write(rebuild);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public static String makeAwsLambdaRequest(String endpoint, String json, HttpServletRequest request) throws Exception {
        SSLContext sslcontext = SSLContexts.custom().build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.1", "TLSv1.2" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
            HttpPost postRequest = new HttpPost(endpoint);
            rebuildForwardHeader(postRequest, request);
            postRequest.addHeader("Content-type", "application/json; charset=UTF-8");
            StringEntity input = new StringEntity(json, "UTF-8");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error("{}", e);
        }
        return "";
    }

    private static void rebuildForwardHeader(HttpPost postRequest, HttpServletRequest request) {
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            postRequest.addHeader(key, value);
        }
    }

}
