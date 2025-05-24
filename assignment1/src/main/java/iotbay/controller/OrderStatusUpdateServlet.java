package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import iotbay.dao.DBConnector;
import iotbay.dao.OrderDAO;
import iotbay.model.Order;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateOrderStatus")
public class OrderStatusUpdateServlet extends HttpServlet {
    private DBConnector db;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        try {
            db = new DBConnector();
            Connection conn = db.openConnection();
            orderDAO = new OrderDAO(conn);
        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("DBConnector initialization failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) (session != null ? session.getAttribute("user") : null);

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        String status = request.getParameter("status");

        if (orderIdParam == null || status == null || status.trim().isEmpty()) {
            session.setAttribute("message", "Invalid status or order ID.");
            response.sendRedirect("orderlist");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            Order order = (Order) orderDAO.getOrder(orderId);

            if (order == null || !user.getEmail().equals(order.getUserEmail())) {
                session.setAttribute("message", "You are not authorized to update this order.");
                response.sendRedirect("orderlist");
                return;
            }

            order.setStatus(status);
            orderDAO.updateOrder(order);
            session.setAttribute("message", "Order status updated!");
            response.sendRedirect("orderDetails?orderId=" + orderId);

        } catch (Exception e) {
            session.setAttribute("message", "Failed to update order status.");
            response.sendRedirect("orderlist");
        }
    }

    @Override
    public void destroy() {
        try {
            if (db != null) db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}