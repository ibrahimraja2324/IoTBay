package iotbay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.User;


public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }
    
    // This method is used to find a user by email and password
    // It is used for user login
    public User findUser(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ? AND IsActive = true";
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
            boolean isActive = rs.getBoolean("IsActive");
            User user = new User(firstName, lastName, userEmail, userPassword, phoneNumber, role);
            user.setActive(isActive);
            return user;
        }
        return null;
    }

    // Find a user by email without password
    // Can be used for checking if the email already exists during registration
    public User findUser(String email) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Email = ?";
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
        String sql = "INSERT INTO Users (Email, Password, FirstName, LastName, PhoneNumber) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getLastName());
        ps.setString(5, user.getPhone());
        ps.setString(6, user.getRole());
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }
    
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET FirstName = ?, LastName = ?, Password = ?, PhoneNumber = ? WHERE Email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getPhone());
        ps.setString(5, user.getEmail());
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }

    public boolean toggleUserStatus(String email) throws SQLException {
        String sql = "UPDATE Users SET IsActive = NOT IsActive WHERE Email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }

    public boolean deleteUser(String email) throws SQLException {
        String sql = "DELETE FROM Users WHERE Email = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            users.add(extractUser(rs));
        }
        return users;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String email = rs.getString("Email");
        String password = rs.getString("Password");
        String phone = rs.getString("PhoneNumber");
        String role = rs.getString("Role");
        boolean isActive = rs.getBoolean("IsActive");

        User user = new User(firstName, lastName, email, password, phone, role);
        user.setActive(isActive);
        return user;
    }

    public User login(String email, String password) throws SQLException {
        return findUser(email, password);
    }

    public boolean updateProfile(User user, String firstName, String lastName, String phone, String newPassword, String oldPassword) throws SQLException {
        if (!user.getPassword().equals(oldPassword)) {
            return false;
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setPassword(newPassword);
        return updateUser(user);
    }

    public boolean deleteAccount(User user, String password) throws SQLException {
        if (!user.getPassword().equals(password)) {
            return false;
        }
        return deleteUser(user.getEmail());
    }

    public boolean toggleAccountStatus(User user) throws SQLException {
        return toggleUserStatus(user.getEmail());
    }

}
