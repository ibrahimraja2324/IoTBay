package iotbay.dao;

import iotbay.model.*;
import iotbay.dao.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Validate a user login by checking email and password
    public User validateUser(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        DBConnector db = null;
        try {
            db = new DBConnector();
            Connection conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
                user.setEmail(rs.getString("Email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.closeConnection(); // Ensure the connection is closed
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return user;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO User (FirstName, LastName, Email, Password, PhoneNumber) VALUES (?, ?, ?, ?, ?)";
        DBConnector db = null;
        try {
            db = new DBConnector();
            Connection conn = db.openConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhone());

            stmt.executeUpdate();
            return true; // Registration successful
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
            return false; // Registration failed
        } finally {
            if (db != null) {
                try {
                    db.closeConnection(); // Ensure the connection is closed
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // You can add more methods to register new users, update user info, etc.
}