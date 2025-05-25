package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import iotbay.dao.DBConnector;
import iotbay.dao.OrderDAO;
import iotbay.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/updateOrderQuantity")
public class UpdateOrderQtyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int orderId;
        String quantityStr;

        // Step 1: Parse input
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
            quantityStr = request.getParameter("quantity");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid order ID or quantity.");
            request.getRequestDispatcher("orderlist.jsp").forward(request, response);
            return;
        }

        // Step 2: Validate quantity
        int qty;
        try {
            qty = Integer.parseInt(quantityStr);
            if (qty < 1) {
                request.setAttribute("errorMessage", "Quantity must be at least 1.");
                request.getRequestDispatcher("orderlist.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Quantity format is not valid.");
            request.getRequestDispatcher("orderlist.jsp").forward(request, response);
            return;
        }

        DBConnector db = null;
        try {
            // Step 3: DB connection and update
            db = new DBConnector();
            Connection conn = db.openConnection();
            OrderDAO orderDAO = new OrderDAO(conn);

            Order order = orderDAO.getOrder(orderId);
            if (order != null) {
                order.setQuantity(String.valueOf(qty));
                boolean updated = orderDAO.updateOrder(order);

                if (!updated) {
                    request.setAttribute("errorMessage", "Order update failed.");
                    request.getRequestDispatcher("orderlist.jsp").forward(request, response);
                    return;
                }

                // Success message stored in session so it survives redirect
                HttpSession session = request.getSession();
                session.setAttribute("successMessage", "Order quantity updated successfully.");
            }

            // Step 4: Redirect to refresh list
            response.sendRedirect("listOrders");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("orderlist.jsp").forward(request, response);
        } finally {
            // Step 5: Cleanup
            if (db != null) {
                try {
                    db.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}