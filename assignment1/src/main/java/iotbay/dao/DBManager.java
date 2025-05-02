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
        String query = "SELECT * FROM User WHERE email='" + email + "' AND password='" + password + "'";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            String userEmail = rs.getString("email");
            String userName = rs.getString("name");
            String userPassword = rs.getString("password");
            String userGender = rs.getString("gender");
            String userFavcol = rs.getString("favcol");
            return new User(userEmail, userName, userPassword, userGender, userFavcol);
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