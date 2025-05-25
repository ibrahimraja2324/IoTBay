package iotbay.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import iotbay.dao.DBConnector;
import iotbay.dao.PaymentDAO;
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
        User user = (User) session.getAttribute("user");

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
}