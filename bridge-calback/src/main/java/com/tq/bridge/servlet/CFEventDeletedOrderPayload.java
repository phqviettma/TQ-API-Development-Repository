package com.tq.bridge.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implememts class for receiving the destroy order event
 */
@WebServlet("/clickfunnel/deleted/order")
public class CFEventDeletedOrderPayload extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String ORDER_EVENT_DELETED_RESOURSE = "order?event=order-deleted";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CFEventDeletedOrderPayload() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("True Quit for deleting Order.");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CFEventUtils.makeRequest(request, response, ORDER_EVENT_DELETED_RESOURSE);
    }

}
