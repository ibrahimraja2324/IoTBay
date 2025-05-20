package iotbay.dao;

import iotbay.model.User;
import java.sql.*;


public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }
    
    // This method is used to find a user by email and password
    // It is used for user login
    public User findUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String userEmail = rs.getString("Email");
            String userPassword = rs.getString("Password");
            String phoneNumber = rs.getString("PhoneNumber");
            String role = rs.getString("Role");
            return new User(firstName, lastName, userEmail, userPassword, phoneNumber, role);
        }
        return null;
    }

    // This method is used to find a user by email only, without password
    // It can be used for checking if the email already exists during registration
    public User findUser(String email) throws SQLException {
        String sql = "SELECT * FROM User WHERE Email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String userEmail = rs.getString("Email");
            String userPassword = rs.getString("Password");
            String phoneNumber = rs.getString("PhoneNumber");
            String role = rs.getString("Role");
            return new User(firstName, lastName, userEmail, userPassword, phoneNumber, role);
        }
        return null;
    }

    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO User (Email, Password, FirstName, LastName, PhoneNumber) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getLastName());
        ps.setString(5, user.getPhone());
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }
    
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE User SET FirstName = ?, LastName = ?, Password = ?, PhoneNumber = ? WHERE Email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getPhone());
        ps.setString(5, user.getEmail());
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteUser(String email) throws SQLException {
        String sql = "DELETE FROM User WHERE Email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }
}
