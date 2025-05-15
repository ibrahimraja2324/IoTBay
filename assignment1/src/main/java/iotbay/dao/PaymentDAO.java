package iotbay.dao;

import iotbay.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection conn;
 
    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }
    
    public List<Payment> findPaymentMethods(int userId) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE userId = ? AND orderId = 0";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        List<Payment> paymentMethods = new ArrayList<>();
        while (rs.next()){
            int paymentId = rs.getInt("paymentId");
            int orderId = rs.getInt("orderId");
            String method = rs.getString("paymentMethod");
            String cardDetails = rs.getString("cardDetails");
            double amount = rs.getDouble("amount");
            String date = rs.getString("date");
            Payment p = new Payment(paymentId, orderId, method, cardDetails, amount, date, userId);
            paymentMethods.add(p);
        }
        return paymentMethods;
    }
    
    public void addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment (orderId, paymentMethod, cardDetails, amount, date, userId) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, payment.getOrderId());
        ps.setString(2, payment.getPaymentMethod());
        ps.setString(3, payment.getCardDetails());
        ps.setDouble(4, payment.getAmount());
        ps.setString(5, payment.getDate());
        ps.setInt(6, payment.getUserId());
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
            String method = rs.getString("paymentMethod");
            String cardDetails = rs.getString("cardDetails");
            double amount = rs.getDouble("amount");
            String date = rs.getString("date");
            int userId = rs.getInt("userId");
            return new Payment(paymentId, orderId, method, cardDetails, amount, date, userId);
        }
        return null;
    }
    
    public void updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE Payment SET orderId = ?, paymentMethod = ?, cardDetails = ?, amount = ?, date = ?, userId = ? WHERE paymentId = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, payment.getOrderId());
        ps.setString(2, payment.getPaymentMethod());
        ps.setString(3, payment.getCardDetails());
        ps.setDouble(4, payment.getAmount());
        ps.setString(5, payment.getDate());
        ps.setInt(6, payment.getUserId());
        ps.setInt(7, payment.getPaymentId());
        ps.executeUpdate();
    }
}
