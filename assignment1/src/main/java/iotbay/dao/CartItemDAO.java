package iotbay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.CartItem;
import iotbay.model.Device;

/**
 * DAO for managing saved cart items in the database.
 * Operates on the 'saved_cartitem' table, loading items by userEmail.
 */
public class CartItemDAO {
    private final Connection conn;

    public CartItemDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Adds a new item to the user's saved cart.
     * Does not update quantity if the item already exists.
     */
    public void addItem(String email, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO saved_cartitem (userEmail, productId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all saved cart items for the given user.
     * Each CartItem includes the full Device info from the DeviceDAO.
     */
    public List<CartItem> getCartItems(String email, DeviceDAO deviceDAO) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT * FROM saved_cartitem WHERE userEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("productId");
                int qty = rs.getInt("quantity");

                // Fetch device details
                Device device = deviceDAO.getDeviceById(productId);

                if (device != null) {
                    items.add(new CartItem(device, qty));
                }
            }
        }
        return items;
    }

    /**
     * Deletes all items from a user's cart after checkout or manual clear.
     */
    public void clearCart(String email) throws SQLException {
        String sql = "DELETE FROM saved_cartitem WHERE userEmail = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a specific item from a user's cart.
     */
    public void removeItem(String email, int productId) throws SQLException {
        String sql = "DELETE FROM saved_cartitem WHERE userEmail = ? AND productId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates quantity of a specific cart item for a user.
     */
    public void updateItemQuantity(String email, int productId, int quantity) throws SQLException {
        String sql = "UPDATE saved_cartitem SET quantity = ? WHERE userEmail = ? AND productId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, email);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }
}