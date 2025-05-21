package iotbay.dao;

import iotbay.model.Shipment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {
    private Connection conn;
    
    public ShipmentDAO(Connection conn) {
        this.conn = conn;
    }
    
    public boolean createShipment(Shipment shipment) throws SQLException {
        String sql = "INSERT INTO Shipment (orderId, shipmentMethod, shipmentDate, address, status, userEmail) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, shipment.getOrderId());
        ps.setString(2, shipment.getShipmentMethod());
        ps.setString(3, shipment.getShipmentDate());
        ps.setString(4, shipment.getAddress());
        ps.setString(5, shipment.getStatus());
        ps.setString(6, shipment.getUserEmail());
        
        int affectedRows = ps.executeUpdate();
        
        if (affectedRows == 0) {
            return false;
        }
        
        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            shipment.setShipmentId(generatedKeys.getInt(1));
        }
        
        return true;
    }
    
    public Shipment findShipment(int shipmentId) throws SQLException {
        String sql = "SELECT * FROM Shipment WHERE shipmentId = ?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, shipmentId);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return extractShipmentFromResultSet(rs);
        }
        
        return null;
    }
    
    public List<Shipment> findShipmentsByUser(String userEmail) throws SQLException {
        String sql = "SELECT * FROM Shipment WHERE userEmail = ? ORDER BY shipmentDate DESC";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, userEmail);
        
        ResultSet rs = ps.executeQuery();
        List<Shipment> shipments = new ArrayList<>();
        
        while (rs.next()) {
            shipments.add(extractShipmentFromResultSet(rs));
        }
        
        return shipments;
    }
    
    public List<Shipment> findShipmentsByOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM Shipment WHERE orderId = ? ORDER BY shipmentDate DESC";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, orderId);
        
        ResultSet rs = ps.executeQuery();
        List<Shipment> shipments = new ArrayList<>();
        
        while (rs.next()) {
            shipments.add(extractShipmentFromResultSet(rs));
        }
        
        return shipments;
    }
    
    public List<Shipment> searchShipments(String userEmail, String searchTerm, String searchBy) throws SQLException {
        String sql = "SELECT * FROM Shipment WHERE userEmail = ?";
        
        // Add search condition based on the searchBy parameter
        if (searchBy != null && !searchBy.isEmpty() && searchTerm != null && !searchTerm.isEmpty()) {
            if (searchBy.equals("id")) {
                sql += " AND shipmentId = ?";
            } else if (searchBy.equals("date")) {
                sql += " AND shipmentDate LIKE ?";
            }
        }
        
        sql += " ORDER BY shipmentDate DESC";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, userEmail);
        
        // Set search parameter if needed
        if (searchBy != null && !searchBy.isEmpty() && searchTerm != null && !searchTerm.isEmpty()) {
            if (searchBy.equals("id")) {
                ps.setInt(2, Integer.parseInt(searchTerm));
            } else if (searchBy.equals("date")) {
                ps.setString(2, "%" + searchTerm + "%");
            }
        }
        
        ResultSet rs = ps.executeQuery();
        List<Shipment> shipments = new ArrayList<>();
        
        while (rs.next()) {
            shipments.add(extractShipmentFromResultSet(rs));
        }
        
        return shipments;
    }
    
    public boolean updateShipment(Shipment shipment) throws SQLException {
        String sql = "UPDATE Shipment SET orderId = ?, shipmentMethod = ?, shipmentDate = ?, " +
                     "address = ?, status = ?, userEmail = ? WHERE shipmentId = ?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, shipment.getOrderId());
        ps.setString(2, shipment.getShipmentMethod());
        ps.setString(3, shipment.getShipmentDate());
        ps.setString(4, shipment.getAddress());
        ps.setString(5, shipment.getStatus());
        ps.setString(6, shipment.getUserEmail());
        ps.setInt(7, shipment.getShipmentId());
        
        int affectedRows = ps.executeUpdate();
        
        return affectedRows > 0;
    }
    
    public boolean deleteShipment(int shipmentId) throws SQLException {
        String sql = "DELETE FROM Shipment WHERE shipmentId = ?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, shipmentId);
        
        int affectedRows = ps.executeUpdate();
        
        return affectedRows > 0;
    }
    
    private Shipment extractShipmentFromResultSet(ResultSet rs) throws SQLException {
        int shipmentId = rs.getInt("shipmentId");
        int orderId = rs.getInt("orderId");
        String shipmentMethod = rs.getString("shipmentMethod");
        String shipmentDate = rs.getString("shipmentDate");
        String address = rs.getString("address");
        String status = rs.getString("status");
        String userEmail = rs.getString("userEmail");
        
        return new Shipment(shipmentId, orderId, shipmentMethod, shipmentDate, address, status, userEmail);
    }
}