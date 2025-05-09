package iotbay.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DB {   
    protected String URL = "jdbc:sqlite:" + System.getProperty("catalina.base") + "/webapps/assignment1/WEB-INF/database/database.db";
    protected String driver = "org.sqlite.JDBC";
    protected Connection conn;

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(driver); // Load the SQLite driver
            conn = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite database successfully.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("SQLite Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to connect to the SQLite database.");
        }
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection to SQLite closed successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}