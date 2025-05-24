package iotbay.dao;

import iotbay.model.User;
import java.sql.*;


public class DBManager {

    private Connection conn;
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
        String query = "SELECT * FROM User WHERE Email='" + email + "' AND Password='" + password + "'";
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            String FirstName = rs.getString("FirstName");
            String LastName = rs.getString("LastName");
            String userEmail = rs.getString("Email");
            String userPassword = rs.getString("Password");
            String userPhone = rs.getString("PhoneNumber");
            String userRole = rs.getString("Role");
            return new User(FirstName, LastName, userEmail, userPassword, userPhone, userRole);
        }
        return null;
    }

    // Other user methods (addUser, updateUser, deleteUser)...

}
