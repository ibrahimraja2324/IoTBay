package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import iotbay.dao.DBConnector;
import iotbay.dao.OrderDAO;
import iotbay.dao.PaymentDAO;
import iotbay.model.Cart;
import iotbay.model.Payment;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private PaymentDAO paymentDAO;

    @Override
    public void init() {
        try {
            DBConnector connector = new DBConnector();
            Connection conn = connector.openConnection();
            paymentDAO = new PaymentDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            try {
                List<Payment> methods = paymentDAO.findPaymentMethods(user.getEmail());
                request.setAttribute("savedMethods", methods);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
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

        if (deliveryAddress == null || deliveryAddress.trim().isEmpty() ||
            paymentMethod == null || paymentMethod.trim().isEmpty()) {
            session.setAttribute("checkoutError", "Please provide all required information.");
            response.sendRedirect("checkout.jsp");
            return;
        }

        try {
            DBConnector db = new DBConnector();
            Connection conn = db.openConnection();
            OrderDAO orderDAO = new OrderDAO(conn);
            boolean success = orderDAO.insertOrder(user.getEmail(), cart, deliveryAddress, paymentMethod);
            if (success) {
                session.setAttribute("cart", new Cart(new ArrayList<>()));
                session.setAttribute("orderConfirmationMessage", "Your order was successfully placed!");
                response.sendRedirect(request.getContextPath() + "/orderConfirmation.jsp");
            } else {
                session.setAttribute("checkoutError", "Order creation failed. Please try again.");
                response.sendRedirect("checkout.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("checkoutError", "An error occurred while creating the order.");
            response.sendRedirect("checkout.jsp");
        }
    }
}