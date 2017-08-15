package com.tq.bridge.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EventCreatedOrderPayload
 */
@WebServlet("/clickfunnel/created/order")
public class CFEventCreatedOrderPayload extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String ORDER_EVENT_CREATED_RESOURSE = "order?event=order-created";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CFEventCreatedOrderPayload() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("True Quit for creating Order");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CFEventUtils.makeRequest(request, response, ORDER_EVENT_CREATED_RESOURSE);
    }

}
