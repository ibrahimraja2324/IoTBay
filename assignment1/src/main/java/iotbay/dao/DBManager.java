package iotbay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import iotbay.model.User;


public class DBManager {

    private final Connection conn;
    private Statement st;

    public DBManager(Connection conn) throws SQLException {
        this.conn = conn;
        st = conn.createStatement();
    }
    
    public Connection getConnection() {
        return conn;
    }

    // ----- User Operations -----
    public User findUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email='" + email + "' AND Password='" + password + "' AND IsActive=true";
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            String FirstName = rs.getString("FirstName");
            String LastName = rs.getString("LastName");
            String userEmail = rs.getString("Email");
            String userPassword = rs.getString("Password");
            String userPhone = rs.getString("PhoneNumber");
            String userRole = rs.getString("Role");
            boolean isActive = rs.getBoolean("IsActive");
            User user = new User(FirstName, LastName, userEmail, userPassword, userPhone, userRole);
            user.setActive(isActive);
            return user;
        }
        return null;
    }

    // Other user methods (addUser, updateUser, deleteUser)...

}
