package iotbay.dao;

import iotbay.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection conn;
    
    public OrderDAO(Connection conn) {
        this.conn = conn;
    }
    
    public List<Order> findOrdersByUser(String email) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE userEmail = ? ORDER BY orderDate DESC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        List<Order> orders = new ArrayList<>();
        while (rs.next()){
            int orderId = rs.getInt("orderId");
            String orderDate = rs.getString("orderDate");
            String status = rs.getString("status");
            double totalAmount = rs.getDouble("totalAmount");
            Order order = new Order(orderId, orderDate, status, totalAmount, email);
            orders.add(order);
        }
        return orders;
    }
    
    public boolean createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (orderDate, status, totalAmount, userEmail) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, order.getOrderDate());
        ps.setString(2, order.getStatus());
        ps.setDouble(3, order.getTotalAmount());
        ps.setString(4, order.getUserEmail());
        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0){
            return false;
        }
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            order.setOrderId(rs.getInt(1));
        }
        return true;
    }
    
    
}
