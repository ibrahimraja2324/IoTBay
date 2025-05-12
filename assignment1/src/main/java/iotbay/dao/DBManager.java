package iotbay.dao;

import iotbay.model.*;
import java.sql.*;


/* 
* DBManager is the primary DAO class to interact with the database. 
* Complete the existing methods of this classes to perform CRUD operations with the db.
*/

public class DBManager {

    private Statement st;

    public DBManager(Connection conn) throws SQLException {
        st = conn.createStatement();
    }

    // Find user by email and password in the database
    public User findUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email='" + email + "' AND Password='" + password + "'";

        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            String FirstName = rs.getString("FirstName");
            String LastName = rs.getString("LastName");
            String userEmail = rs.getString("Email");
            String userPassword = rs.getString("Password");
            String userPhone = rs.getString("PhoneNumber");
            return new User(FirstName, LastName, userEmail, userPassword, userPhone);
        }
        return null;
    }

    // Add a user-data into the database
    public void addUser(String email, String name, String password, String gender, String favcol) throws SQLException { // code
                                                                                                                        // for
                                                                                                                        // add-operation
        String query = "INSERT INTO User (email, name, password, gender, favcol) VALUES ('" + email + "', '" + name + "', '" + password + "', '" + gender + "', '" + favcol + "')";
        st.executeUpdate(query);

    }

    // update a user details in the database
    public void updateUser(String email, String name, String password, String gender, String favcol)
            throws SQLException {
        String query = "UPDATE User SET name='" + name + "', password='" + password + "', gender='" + gender + "', favcol='" + favcol + "' WHERE email='" + email + "'";
        st.executeUpdate(query);

    }

    // delete a user from the database
    public void deleteUser(String email) throws SQLException {
        String query = "DELETE FROM User WHERE email='" + email + "'";
        st.executeUpdate(query);

    }

}