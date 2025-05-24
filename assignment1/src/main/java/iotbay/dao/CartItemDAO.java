package iotbay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.CartItem;
import iotbay.model.Device;

public class CartItemDAO {
    private final Connection conn;

    public CartItemDAO(Connection conn) {
        this.conn = conn;
    }

    public void addItem(String email, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO CartItems (userEmail, productId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public List<CartItem> getCartItems(String email, DeviceDAO productDAO) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM CartItems WHERE userEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("productId");
                int qty = rs.getInt("quantity");
                Device product = productDAO.getDeviceById(productId);
                items.add(new CartItem(product, qty));
            }
        }
        return items;
    }

    public void clearCart(String email) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM CartItems WHERE userEmail = ?")) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }
}