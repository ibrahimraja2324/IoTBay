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

    // READ - Get a device by ID
    public Device getDeviceById(int id) throws SQLException {
        String sql = "SELECT * FROM Device WHERE ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractDevice(rs);
                }
            }
        }
        return null;
    }

    // READ - Search by name and/or type (advanced search)
    public List<Device> searchDevices(String name, String type) throws SQLException {
        List<Device> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Device WHERE 1=1");
    
        if (name != null && !name.isEmpty()) {
            sql.append(" AND Name LIKE ?");
        }
    
        if (type != null && !type.isEmpty()) {
            sql.append(" AND Type = ?");
        }
    
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
    
            if (name != null && !name.isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }
    
            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }
    
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
