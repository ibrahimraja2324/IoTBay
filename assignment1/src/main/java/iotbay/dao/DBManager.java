package iotbay.dao;

import iotbay.model.Payment;
import iotbay.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private Connection conn;
    private Statement st;

    public DBManager(Connection conn) throws SQLException {
        this.conn = conn;
        st = conn.createStatement();
    }
    
    public Connection getConnection() {
        return conn;
    }

    // ----- User Operations -----
    public User findUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email='" + email + "' AND Password='" + password + "'";
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            String FirstName = rs.getString("FirstName");
            String LastName = rs.getString("LastName");
            String userEmail = rs.getString("Email");
            String userPassword = rs.getString("Password");
            String userPhone = rs.getString("PhoneNumber");
            String userRole = rs.getString("Role");
            return new User(FirstName, LastName, userEmail, userPassword, userPhone, userRole);
        }
        return null;
    }

    // Other user methods (addUser, updateUser, deleteUser)...

    // ----- Payment Operations -----
    public List<Payment> findPayments(String userEmail) throws SQLException {
        String query = "SELECT * FROM Payment WHERE userEmail=" + userEmail;
        ResultSet rs = st.executeQuery(query);
        List<Payment> payments = new ArrayList<>();
        while (rs.next()) {
            int paymentId = rs.getInt("paymentId");
            int orderId = rs.getInt("orderId");
            String paymentMethod = rs.getString("paymentMethod");
            String cardDetails = rs.getString("cardDetails");
            double amount = rs.getDouble("amount");
            String date = rs.getString("date");
            Payment p = new Payment(paymentId, orderId, paymentMethod, cardDetails, amount, date, userEmail);
            payments.add(p);
        }
        return payments;
    }
    
    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO Payment (orderId, paymentMethod, cardDetails, amount, date, userEmail) VALUES ("
                + payment.getOrderId() + ", '"
                + payment.getPaymentMethod() + "', '"
                + payment.getCardDetails() + "', "
                + payment.getAmount() + ", '"
                + payment.getDate() + "', "
                + payment.getUserEmail() + ")";
        st.executeUpdate(query);
    }
    
    public Payment findPayment(int paymentId) throws SQLException {
        String query = "SELECT * FROM Payment WHERE paymentId=" + paymentId;
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            int orderId = rs.getInt("orderId");
            String paymentMethod = rs.getString("paymentMethod");
            String cardDetails = rs.getString("cardDetails");
            double amount = rs.getDouble("amount");
            String date = rs.getString("date");
            String userEmail = rs.getString("userEmail");
            return new Payment(paymentId, orderId, paymentMethod, cardDetails, amount, date, userEmail);
        }
        return null;
    }
    
    public void updatePayment(Payment payment) throws SQLException {
        String query = "UPDATE Payment SET orderId=" + payment.getOrderId()
                + ", paymentMethod='" + payment.getPaymentMethod() + "'"
                + ", cardDetails='" + payment.getCardDetails() + "'"
                + ", amount=" + payment.getAmount()
                + ", date='" + payment.getDate() + "'"
                + ", userEmail=" + payment.getUserEmail()
                + " WHERE paymentId=" + payment.getPaymentId();
        st.executeUpdate(query);
    }
    
    public void deletePayment(int paymentId) throws SQLException {
        String query = "DELETE FROM Payment WHERE paymentId=" + paymentId;
        st.executeUpdate(query);
    }
    
   

    public List<Payment> findPaymentMethods(String userEmail) throws SQLException {
        String query = "SELECT * FROM Payment WHERE userId = " + userEmail + " AND orderId = 0";
        ResultSet rs = st.executeQuery(query);
        List<Payment> paymentMethods = new ArrayList<>();
        while (rs.next()) {
            int paymentId = rs.getInt("paymentId");
            int orderId = rs.getInt("orderId"); // 0 for payment method records
            String paymentMethod = rs.getString("paymentMethod");
            String cardDetails = rs.getString("cardDetails");
            double amount = rs.getDouble("amount"); // normally 0
            String date = rs.getString("date"); // could be an expiry date
            Payment p = new Payment(paymentId, orderId, paymentMethod, cardDetails, amount, date, userEmail);
            paymentMethods.add(p);
        }
        return paymentMethods;
    }
    
    public List<Payment> findPaymentHistory(String userEmail) throws SQLException {
        String query = "SELECT * FROM Payment WHERE userEmail = " + userEmail + " AND orderId > 0 ORDER BY date DESC";
        ResultSet rs = st.executeQuery(query);
        List<Payment> orders = new ArrayList<>();
        while (rs.next()) {
            int paymentId = rs.getInt("paymentId");
            int orderId = rs.getInt("orderId");
            String paymentMethod = rs.getString("paymentMethod");
            String cardDetails = rs.getString("cardDetails");
            double amount = rs.getDouble("amount");
            String date = rs.getString("date");
            Payment p = new Payment(paymentId, orderId, paymentMethod, cardDetails, amount, date, userEmail);
            orders.add(p);
        }
        return orders;
    }
}
