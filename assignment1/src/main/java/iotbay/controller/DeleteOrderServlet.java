package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import iotbay.dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deleteOrder")
public class DeleteOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int orderId = Integer.parseInt(request.getParameter("orderId"));

        try {
            Connection conn = (Connection) getServletContext().getAttribute("dbConnection");
            OrderDAO orderDAO = new OrderDAO(conn);
            orderDAO.deleteOrder(orderId);

            response.sendRedirect("listOrders");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to delete order.");
            request.getRequestDispatcher("orderlist.jsp").forward(request, response);
        }
    }
}