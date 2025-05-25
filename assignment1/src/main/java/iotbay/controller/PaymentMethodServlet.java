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
import java.util.stream.Collectors;

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
    
    
        String search = request.getParameter("search");
        
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        List<Payment> paymentMethods = paymentDAO.findPaymentMethods(currentUser.getEmail());
        
    
        if (search != null && !search.trim().isEmpty()) {
            final String searchLower = search.trim().toLowerCase();
            paymentMethods = paymentMethods.stream().filter(pm -> {
                boolean nameMatches = pm.getCardHolderName().toLowerCase().contains(searchLower);
                
            
                boolean cardMatches = false;
                String cardNumber = pm.getCardNumber();
                if (cardNumber != null && cardNumber.length() >= 4) {
                    String lastFour = cardNumber.substring(cardNumber.length() - 4);
                    cardMatches = lastFour.contains(search);
                }
                
                return nameMatches || cardMatches;
            }).collect(Collectors.toList());
        }
        
        request.setAttribute("paymentMethods", paymentMethods);
        request.getRequestDispatcher("payment-methods.jsp").forward(request, response);
    }   

    

    private void addPaymentMethod(HttpServletRequest request, HttpServletResponse response, DBManager manager)
        throws ServletException, IOException, SQLException {
    HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String paymentMethod = request.getParameter("paymentMethod");
    String cardHolderName = request.getParameter("cardHolderName");
    String cardNumber = request.getParameter("cardNumber");
    String cvv = request.getParameter("cvv");
    String expiryInput = request.getParameter("expiryDate");

    iotbay.controller.Validator validator = new iotbay.controller.Validator();
    boolean hasError = false;
    if (!validator.validatePaymentMethod(paymentMethod)) {
        request.setAttribute("paymentMethodError", "Please select a valid payment method.");
        hasError = true;
    }
    if (!validator.validateCardHolderName(cardHolderName)) {
        request.setAttribute("cardHolderNameError", "Card holder name is required.");
        hasError = true;
    }
    if (!validator.validateCardNumber(cardNumber)) {
        request.setAttribute("cardNumberError", "Card number must be exactly 16 digits.");
        hasError = true;
    }
    if (!validator.validateCVV(cvv)) {
        request.setAttribute("cvvError", "CVV must be 3 or 4 digits.");
        hasError = true;
    }
    if (!validator.validateExpiryDateFuture(expiryInput)) {
        request.setAttribute("expiryDateError", "Expiry date must be in the future and in the format YYYY-MM.");
        hasError = true;
    }
    
    request.setAttribute("paymentMethodInput", paymentMethod);
    request.setAttribute("cardHolderNameInput", cardHolderName);
    request.setAttribute("cardNumberInput", cardNumber);
    request.setAttribute("cvvInput", cvv);
    request.setAttribute("expiryDateInput", expiryInput);

    if (hasError) {
        request.getRequestDispatcher("payment-dashboard.jsp").forward(request, response);
        return;
    }
    
    String expiryDateConverted = "";
    if(expiryInput != null && expiryInput.matches("\\d{4}-\\d{2}")){
        String[] parts = expiryInput.split("-");
        expiryDateConverted = parts[1] + "/" + parts[0].substring(2);
    } else {
        expiryDateConverted = expiryInput;
    }
    
    Connection conn = manager.getConnection();
    PaymentDAO paymentDAO = new PaymentDAO(conn);
    Payment newPayment = new Payment(0, 0, paymentMethod, cardHolderName, cardNumber, cvv, expiryDateConverted, 0.0, currentUser.getEmail());
    
    try {
        paymentDAO.addPayment(newPayment);
        response.sendRedirect("payment-dashboard.jsp");
    } catch (SQLException ex) {
        if(ex.getMessage().contains("Card number already exists")) {
            request.setAttribute("cardNumberError", "Card number already exists.");
        } else {
            request.setAttribute("cardNumberError", "Error adding payment: " + ex.getMessage());
        }
        request.getRequestDispatcher("payment-dashboard.jsp").forward(request, response);
    }
}


    
    private void deletePaymentMethod(HttpServletRequest request, HttpServletResponse response, DBManager manager)
            throws IOException, SQLException {
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        Connection conn = manager.getConnection();
        PaymentDAO paymentDAO = new PaymentDAO(conn);
        paymentDAO.deletePayment(paymentId);
        response.sendRedirect("PaymentMethodServlet?action=list");
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
        throws ServletException, IOException, SQLException {
    int paymentId = Integer.parseInt(request.getParameter("paymentId"));
    String paymentMethod = request.getParameter("paymentMethod");
    String cardHolderName = request.getParameter("cardHolderName");
    String cardNumber = request.getParameter("cardNumber");
    String cvv = request.getParameter("cvv");
    String expiryInput = request.getParameter("expiryDate");

    iotbay.controller.Validator validator = new iotbay.controller.Validator();
    boolean hasError = false;
    if (!validator.validatePaymentMethod(paymentMethod)) {
        request.setAttribute("paymentMethodError", "Please select a valid payment method.");
        hasError = true;
    }
    if (!validator.validateCardHolderName(cardHolderName)) {
        request.setAttribute("cardHolderNameError", "Please enter a valid card holder name (e.g., John Doe).");
        hasError = true;
    }
    if (!validator.validateCardNumber(cardNumber)) {
        request.setAttribute("cardNumberError", "Card number must be exactly 16 digits.");
        hasError = true;
    }
    if (!validator.validateCVV(cvv)) {
        request.setAttribute("cvvError", "CVV must be 3 or 4 digits.");
        hasError = true;
    }
    if (!validator.validateExpiryDateFuture(expiryInput)) {
        request.setAttribute("expiryDateError", "Expiry date must be in the future and in the format YYYY-MM.");
        hasError = true;
    }
    
    request.setAttribute("paymentMethodInput", paymentMethod);
    request.setAttribute("cardHolderNameInput", cardHolderName);
    request.setAttribute("cardNumberInput", cardNumber);
    request.setAttribute("cvvInput", cvv);
    request.setAttribute("expiryDateInput", expiryInput);
    
    if (hasError) {
        Payment errorPayment = new Payment(paymentId, 0, paymentMethod, cardHolderName, cardNumber, cvv, expiryInput, 0.0,
                ((User) request.getSession().getAttribute("currentUser")).getEmail());
        request.setAttribute("paymentMethod", errorPayment);
        request.getRequestDispatcher("payment-method-form.jsp").forward(request, response);
        return;
    }
    
   
    String expiryDateConverted = "";
    if(expiryInput != null && expiryInput.matches("\\d{4}-\\d{2}")){
        String[] parts = expiryInput.split("-");
        expiryDateConverted = parts[1] + "/" + parts[0].substring(2);
    } else {
        expiryDateConverted = expiryInput;
    }
    
    HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("currentUser");
    Connection conn = manager.getConnection();
    PaymentDAO paymentDAO = new PaymentDAO(conn);
    Payment updatedPayment = new Payment(paymentId, 0, paymentMethod, cardHolderName, cardNumber, cvv, expiryDateConverted, 0.0, currentUser.getEmail());
    
    try {
        paymentDAO.updatePayment(updatedPayment);
        response.sendRedirect("PaymentMethodServlet?action=list");
    } catch (SQLException ex) {
        if(ex.getMessage().contains("Card number already exists")) {
            request.setAttribute("cardNumberError", "Card number already exists.");
        } else {
            request.setAttribute("cardNumberError", "Error updating payment: " + ex.getMessage());
        }
        request.setAttribute("paymentMethod", updatedPayment);
        request.getRequestDispatcher("payment-method-form.jsp").forward(request, response);
    }
}


}
