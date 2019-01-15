package com.tq.bridge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/clickfunnel/optin/facebook/created/contact")
public class CFEventCreatedOptInFacebookLead extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String CONTACT_EVENT_CREATED_RESOURCE = "contact?event=contact-created&optInType=FacebookLead";

    /**
     * Default constructor.
     */
    public CFEventCreatedOptInFacebookLead() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Opt-in Facebook Lead");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CFEventUtils.makeRequest(request, response, CONTACT_EVENT_CREATED_RESOURCE);
    }
}
