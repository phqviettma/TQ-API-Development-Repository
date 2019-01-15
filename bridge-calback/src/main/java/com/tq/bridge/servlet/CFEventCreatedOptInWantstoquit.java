package com.tq.bridge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/clickfunnel/optin/wantstoquit/created/contact")
public class CFEventCreatedOptInWantstoquit extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String CONTACT_EVENT_CREATED_RESOURCE = "contact?event=contact-created&optInType=Wantstoquit";

    /**
     * Default constructor.
     */
    public CFEventCreatedOptInWantstoquit() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Opt-in Wantstoquit");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CFEventUtils.makeRequest(request, response, CONTACT_EVENT_CREATED_RESOURCE);
    }
}
