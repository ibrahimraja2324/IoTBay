package iotbay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.Payment;

public class PaymentDAO {
    private Connection conn;
 
    public PaymentDAO(Connection conn2) {
        this.conn = conn2;
    }

    private String normalizeCardNumber(String cardNumber) {
    return cardNumber.replaceAll("[^0-9]", "");
    }

    
    public List<Payment> findPaymentMethods(String userEmail) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE userEmail = ? AND orderId = 0";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, userEmail);
        ResultSet rs = ps.executeQuery();
        List<Payment> paymentMethods = new ArrayList<>();
        while (rs.next()){
            int paymentId = rs.getInt("paymentId");
            int orderId = rs.getInt("orderId");
            String paymentMethod = rs.getString("paymentMethod");
            String cardHolderName = rs.getString("cardHolderName");
            String cardNumber = rs.getString("cardNumber");
            String cvv = rs.getString("cvv");
            String expiryDate = rs.getString("expiryDate");
            double amount = rs.getDouble("amount");
            Payment p = new Payment(paymentId, orderId, paymentMethod, cardHolderName, cardNumber, cvv, expiryDate, amount, userEmail);
            paymentMethods.add(p);
        }
        return paymentMethods;
    }
    
    public void addPayment(Payment payment) throws SQLException {
    String normalizedNumber = normalizeCardNumber(payment.getCardNumber());
    
    if (!isCardNumberUnique(normalizedNumber)) {
        throw new SQLException("Card number already exists.");
    }
    
    String sql = "INSERT INTO Payment (orderId, paymentMethod, cardHolderName, cardNumber, cvv, expiryDate, amount, userEmail) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setInt(1, payment.getOrderId());
    ps.setString(2, payment.getPaymentMethod());
    ps.setString(3, payment.getCardHolderName());
    ps.setString(4, normalizedNumber);
    ps.setString(5, payment.getCvv());
    ps.setString(6, payment.getExpiryDate());
    ps.setDouble(7, payment.getAmount());
    ps.setString(8, payment.getUserEmail());
    ps.executeUpdate();
}

    
    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payment WHERE paymentId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, paymentId);
        ps.executeUpdate();
    }
    
    public Payment findPayment(int paymentId) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE paymentId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, paymentId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            int orderId = rs.getInt("orderId");
            String paymentMethod = rs.getString("paymentMethod");
            String cardHolderName = rs.getString("cardHolderName");
            String cardNumber = rs.getString("cardNumber");
            String cvv = rs.getString("cvv");
            String expiryDate = rs.getString("expiryDate");
            double amount = rs.getDouble("amount");
            String userEmail = rs.getString("userEmail");
            return new Payment(paymentId, orderId, paymentMethod, cardHolderName, cardNumber, cvv, expiryDate, amount, userEmail);
        }
        return null;
    }
    
    public void updatePayment(Payment payment) throws SQLException {
    String normalizedNumber = normalizeCardNumber(payment.getCardNumber());
    
    if (!isCardNumberUniqueForUpdate(normalizedNumber, payment.getPaymentId())) {
        throw new SQLException("Card number already exists.");
    }
    
    String sql = "UPDATE Payment SET orderId = ?, paymentMethod = ?, cardHolderName = ?, cardNumber = ?, cvv = ?, expiryDate = ?, amount = ?, userEmail = ? WHERE paymentId = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setInt(1, payment.getOrderId());
    ps.setString(2, payment.getPaymentMethod());
    ps.setString(3, payment.getCardHolderName());
    ps.setString(4, normalizedNumber);
    ps.setString(5, payment.getCvv());
    ps.setString(6, payment.getExpiryDate());
    ps.setDouble(7, payment.getAmount());
    ps.setString(8, payment.getUserEmail());
    ps.setInt(9, payment.getPaymentId());
    ps.executeUpdate();
}


     public boolean isCardNumberUnique(String cardNumber) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM Payment WHERE cardNumber = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cardNumber);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt("count") == 0;
        }
        return true;
    }
    

    public boolean isCardNumberUniqueForUpdate(String cardNumber, int paymentId) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM Payment WHERE cardNumber = ? AND paymentId != ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cardNumber);
        ps.setInt(2, paymentId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt("count") == 0;
        }
        return true;
    }

}

   