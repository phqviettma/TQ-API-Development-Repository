package com.tq.bridge.servlet;

import java.io.BufferedReader;
import java.io.IOException;

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
 * Servlet implementation class CallbackPayLoadContact
 */
@WebServlet("/contact/payload")
public class CallbackPayLoadContact extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(CallbackPayLoadContact.class);

    public static final String API_GATEGAY_CLICK_LAMBDA = "https://yhy9f83hp2.execute-api.us-east-1.amazonaws.com/test/contact";

    /**
     * Default constructor.
     */
    public CallbackPayLoadContact() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder jsonBuff = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jsonBuff.append(line);

        } catch (Exception e) {
            /* error */
            log.error("Brigde Error during reading json request from Click funnel.", e);
        }
        // making request to AWS lambda
        String input = null;
        try {
            input = makeAwsLambdaRequest(API_GATEGAY_CLICK_LAMBDA, jsonBuff.toString());
        } catch (Exception e) {
            log.error("error bridge project.", e);
        }
        String rebuild = input == null ? "{\"error\": 200}" : input;
        response.setContentType("application/json");
        response.getWriter().write(rebuild);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public static String makeAwsLambdaRequest(String endpoint, String json) throws Exception {
        SSLContext sslcontext = SSLContexts.custom().build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.1", "TLSv1.2" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        try (CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();) {
            HttpPost postRequest = new HttpPost(endpoint);
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

}
