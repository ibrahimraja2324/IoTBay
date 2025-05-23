package iotbay.dao;

import iotbay.model.Device;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {
    private final Connection conn;

    public DeviceDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE - Add a new device
    public boolean addDevice(Device device) throws SQLException {
        String sql = "INSERT INTO Device (Name, Type, UnitPrice, Quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, device.getName());
            ps.setString(2, device.getType());
            ps.setDouble(3, device.getUnitPrice());
            ps.setInt(4, device.getQuantity());
            return ps.executeUpdate() > 0;
        }
    }

    // READ - Get all devices
    public List<Device> getAllDevices() throws SQLException {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT * FROM Device";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractDevice(rs));
            }
        }
        return list;
    }

    // READ - Search by name and/or type
    public List<Device> searchDevices(String name, String type) throws SQLException {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT * FROM Device WHERE Name LIKE ? AND Type LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name == null || name.isEmpty() ? "%" : "%" + name + "%");
            ps.setString(2, type == null || type.isEmpty() ? "%" : "%" + type + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractDevice(rs));
                }
            }
        }
        return list;
    }

    // UPDATE - Update existing device
    public boolean updateDevice(Device device) throws SQLException {
        String sql = "UPDATE Device SET Name = ?, Type = ?, UnitPrice = ?, Quantity = ? WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, device.getName());
            ps.setString(2, device.getType());
            ps.setDouble(3, device.getUnitPrice());
            ps.setInt(4, device.getQuantity());
            ps.setInt(5, device.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE - Remove device by ID
    public boolean deleteDevice(int id) throws SQLException {
        String sql = "DELETE FROM Device WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Utility: Convert ResultSet row to Device object
    private Device extractDevice(ResultSet rs) throws SQLException {
        return new Device(
            rs.getInt("ID"),
            rs.getString("Name"),
            rs.getString("Type"),
            rs.getDouble("UnitPrice"),
            rs.getInt("Quantity")
        );
    }
}
