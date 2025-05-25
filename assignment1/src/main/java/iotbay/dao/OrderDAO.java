package iotbay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.Order;

public class OrderDAO {
    private Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Order> findOrdersByUser(String email) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE userEmail = ? ORDER BY orderId DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            return orders;
        }
    }

    public List<Order> searchOrdersByUser(String email, String searchType, String searchTerm) throws SQLException {
        String baseQuery = "SELECT * FROM Orders WHERE userEmail = ? AND ";
        String condition;

        if ("orderId".equalsIgnoreCase(searchType)) {
            condition = "CAST(orderId AS CHAR) LIKE ?";
        } else if ("orderDate".equalsIgnoreCase(searchType)) {
            condition = "orderDate LIKE ?";
        } else {
            return findOrdersByUser(email);
        }

        try (PreparedStatement ps = conn.prepareStatement(baseQuery + condition + " ORDER BY orderId DESC")) {
            ps.setString(1, email);
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            return orders;
        }
    }

    public Order getOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
        }
        return null;
    }

    public boolean createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (status, totalAmount, userEmail, deliveryAddress, paymentMethod) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getStatus());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getUserEmail());
            ps.setString(4, order.getDeliveryAddress());
            ps.setString(5, order.getPaymentMethod());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setOrderId(rs.getInt(1));
                }
            }
            return true;
        }
    }

    public List<Order> listAllOrders() throws SQLException {
        String sql = "SELECT * FROM Orders ORDER BY orderId DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            List<Order> listOrder = new ArrayList<>();
            while (resultSet.next()) {
                listOrder.add(extractOrderFromResultSet(resultSet));
            }
            return listOrder;
        }
    }

    public List<Order> listAllOrders(String sortBy, String sortOrder) throws SQLException {
        if (!"orderId".equalsIgnoreCase(sortBy) && !"orderDate".equalsIgnoreCase(sortBy)) {
            sortBy = "orderId";
        }
        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            sortOrder = "asc";
        }

        String query = "SELECT * FROM Orders ORDER BY " + sortBy + " " + sortOrder;
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            List<Order> listOrder = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                listOrder.add(extractOrderFromResultSet(resultSet));
            }
            return listOrder;
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("orderId"));
        order.setOrderDate(rs.getString("orderDate"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getDouble("totalAmount"));
        order.setUserEmail(rs.getString("userEmail"));
        order.setDeliveryAddress(rs.getString("deliveryAddress"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        return order;
    }

    public boolean updateOrder(Order order) throws SQLException {
        String sql = "UPDATE Orders SET status = ?, totalAmount = ? WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getStatus());
            ps.setDouble(2, order.getTotalAmount());
            ps.setInt(3, order.getOrderId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteOrder(int orderId) throws SQLException {
        String sql = "DELETE FROM Orders WHERE orderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException ignored) {}
    }
}