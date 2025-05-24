package iotbay.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String URL = "jdbc:sqlite:" + System.getProperty("catalina.base") + "/webapps/assignment1/WEB-INF/database/database.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    private Connection conn;

    public DBConnector() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        this.conn = DriverManager.getConnection(URL);
    }

    public Connection openConnection() {
        return this.conn;
    }

    public void closeConnection() throws SQLException {
        if (this.conn != null && !this.conn.isClosed()) {
            this.conn.close();
        }
    }
}