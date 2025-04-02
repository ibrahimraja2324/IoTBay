package iotbay.dao;

import iotbay.model.User;
import iotbay.util.DBConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Validate a user login by checking email and password
    public User validateUser(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM User WHERE EmailAddress = ? AND Password = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);  // In production, use hashed passwords!
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setName(rs.getString("Name"));
                user.setEmailAddress(rs.getString("EmailAddress"));
                // set other fields as needed...
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle exceptions appropriately
        }
        return user;
    }
    
    // You can add more methods to register new users, update user info, etc.
}