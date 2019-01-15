package com.tq.bridge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CFCallbackPayLoad configuration callback URL : http://elasticbeanstalk-env.com/clickfunnel?event=
 * 
 * Here, elasticbeanstalk-env URK : when you deploy an application on Elastic Beanstalk, it will automatically generate the URL associated with environment So,
 * assumed the elasticbeanstalk-env is URL
 */
@WebServlet("/clickfunnel/optin/gdisplay/created/contact")
public class CFEventCreatedOptInGoDisplayAds extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String CONTACT_EVENT_CREATED_RESOURCE = "contact?event=contact-created&optInType=GoogleOptin";

    /**
     * Default constructor.
     */
    public CFEventCreatedOptInGoDisplayAds() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Opt-in Google Display Ads");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CFEventUtils.makeRequest(request, response, CONTACT_EVENT_CREATED_RESOURCE);
    }
}
