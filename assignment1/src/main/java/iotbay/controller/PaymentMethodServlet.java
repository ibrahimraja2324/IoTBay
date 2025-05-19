package iotbay.controller;

import iotbay.dao.DBManager;
import iotbay.dao.PaymentDAO;
import iotbay.model.Payment;
import iotbay.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentMethodServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        if (manager == null) {
            throw new ServletException("DBManager not initialized. Please navigate from the home page.");
        }
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        try {
            switch (action) {
                case "add":
                    addPaymentMethod(request, response, manager);
                    break;
                case "delete":
                    deletePaymentMethod(request, response, manager);
                    break;
                case "edit":
                    showEditForm(request, response, manager);
                    break;
                case "update":
                    updatePaymentMethod(request, response, manager);
                    break;
                case "list":
                default:
                    listPaymentMethods(request, response, manager);
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentMethodServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void listPaymentMethods(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        List<Payment> paymentMethods = paymentDAO.findPaymentMethods(currentUser.getEmail());
        request.setAttribute("paymentMethods", paymentMethods);
        request.getRequestDispatcher("payment-methods.jsp").forward(request, response);
    }
    

    private void addPaymentMethod(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User)session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String paymentMethod = request.getParameter("paymentMethod");
        String cardDetails = request.getParameter("cardDetails");
        String expiryDate = request.getParameter("expiryDate");
  
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        Payment newMethod = new Payment(0, 0, paymentMethod, cardDetails, 0.0, expiryDate, currentUser.getEmail());
        paymentDAO.addPayment(newMethod);
        response.sendRedirect("payment-dashboard.jsp");

    }
    

    private void deletePaymentMethod(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws IOException, SQLException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        paymentDAO.deletePayment(paymentId);
        response.sendRedirect("PaymentMethodServlet?action=list.jsp");
    }
    

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws ServletException, IOException, SQLException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        Payment paymentMethod = paymentDAO.findPayment(paymentId);
        request.setAttribute("paymentMethod", paymentMethod);
        request.getRequestDispatcher("payment-method-form.jsp").forward(request, response);
    }
    

    private void updatePaymentMethod(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws IOException, SQLException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        String paymentMethod = request.getParameter("paymentMethod");
        String cardDetails = request.getParameter("cardDetails");
        String expiryDate = request.getParameter("expiryDate");
        HttpSession session = request.getSession();
        User currentUser = (User)session.getAttribute("currentUser");
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        Payment updatedMethod = new Payment(paymentId, 0, paymentMethod, cardDetails, 0.0, expiryDate, currentUser.getEmail());
        paymentDAO.updatePayment(updatedMethod);
        response.sendRedirect("PaymentMethodServlet?action=list.jsp");
    }
}
