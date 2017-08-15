package com.tq.bridge.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CFEventUpdatedOrderPayload
 */
@WebServlet("/clickfunnel/updated/order")
public class CFEventUpdatedOrderPayload extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public static final String ORDER_EVENT_UPDATED_RESOURCE = "order?event=order-updated";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CFEventUpdatedOrderPayload() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("True Quit for updating Order");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CFEventUtils.makeRequest(request, response, ORDER_EVENT_UPDATED_RESOURCE);
    }

}
