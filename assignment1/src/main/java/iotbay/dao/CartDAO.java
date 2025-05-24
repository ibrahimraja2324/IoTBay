package iotbay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.CartItem;
import iotbay.model.Device;

public class CartDAO {
    private final Connection conn;

    public CartDAO(Connection conn) {
        this.conn = conn;
    }

    // 장바구니 항목 추가
    public void addCartItem(String userEmail, CartItem item) throws SQLException {
        String sql = "INSERT INTO saved_cartitem (userEmail, deviceId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            ps.setInt(2, item.getDevice().getId());
            ps.setInt(3, item.getQuantity());
            ps.executeUpdate();
        }
    }

    // 사용자 이메일로 장바구니 항목 가져오기
    public List<CartItem> getCartItemsByUser(String userEmail) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM saved_cartitem WHERE userEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                DeviceDAO deviceDAO = new DeviceDAO(conn);
                while (rs.next()) {
                    int deviceId = rs.getInt("deviceId");
                    int quantity = rs.getInt("quantity");
                    Device device = deviceDAO.getDeviceById(deviceId);
                    if (device != null) {
                        items.add(new CartItem(device, quantity));
                    }
                }
            }
        }
        return items;
    }

    // 사용자 장바구니 비우기
    public void clearCart(String userEmail) throws SQLException {
        String sql = "DELETE FROM saved_cartitem WHERE userEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            ps.executeUpdate();
        }
    }
}