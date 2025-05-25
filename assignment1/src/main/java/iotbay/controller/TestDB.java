package iotbay.controller;

import java.sql.*;

import java.util.logging.*;

import iotbay.dao.*;

import iotbay.model.User;

public class TestDB {

    public static void main(String[] args) {

        try {



            DBConnector connector = new DBConnector();

            Connection conn = connector.openConnection();

            DBManager db = new DBManager(conn);
            // Test findUser
            System.out.println("Enter email and password to find user:");
            User user = db.findUser("john.doe@example.com", "password123");
            System.out.println("User found: " + user.getFirstName() + ", " + user.getEmail() + ", " + user.getPassword());

            connector.closeConnection();

        } catch (ClassNotFoundException | SQLException ex) {

            Logger.getLogger(TestDB.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

}
