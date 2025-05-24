package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import iotbay.dao.DBConnector;
import iotbay.dao.OrderDAO;
import iotbay.model.Cart;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/saveOrders")
public class OrderSaveServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        String deliveryAddress = request.getParameter("deliveryAddress");
        String paymentMethod = request.getParameter("paymentMethod");

        if (user == null) {
            session.setAttribute("checkoutError", "You must be logged in to place an order.");
            response.sendRedirect("login.jsp");
            return;
        }

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            session.setAttribute("checkoutError", "Your cart is empty.");
            response.sendRedirect("cart.jsp");
            return;
        }

        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()
                || paymentMethod == null || paymentMethod.trim().isEmpty()) {
            session.setAttribute("checkoutError", "Please provide all required information.");
            response.sendRedirect("checkout.jsp");
            return;
        }

        try {
            boolean success = orderDAO.insertOrder(user.getEmail(), cart, deliveryAddress, paymentMethod);
            if (success) {
                session.setAttribute("cart", new Cart());
                session.setAttribute("orderConfirmationMessage", "Your order was successfully placed!");
                response.sendRedirect("orderConfirmation.jsp");
            } else {
                session.setAttribute("checkoutError", "Order creation failed. Please try again.");
                response.sendRedirect("checkout.jsp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("checkoutError", "An error occurred while creating the order.");
            response.sendRedirect("checkout.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("checkout.jsp");
    }

    @Override
    public void destroy() {
        try {
            if (db != null) {
                db.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}