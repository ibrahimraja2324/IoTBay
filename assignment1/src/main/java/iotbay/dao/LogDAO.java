package iotbay.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import iotbay.model.Log;

public class LogDAO {
    private Connection conn;

    public LogDAO(Connection conn) {
        this.conn = conn;
    }

    // Create a new log entry
    public boolean createLog(Log log) throws SQLException {
        String sql = "INSERT INTO AccessLog (Email, Action, Role) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, log.getEmail());
        ps.setString(2, log.getAction());
        ps.setString(3, log.getRole());
        int affectedRows = ps.executeUpdate();
        return affectedRows > 0;
    }

    // Retrieve all log entries
    public ResultSet getAllLogs() throws SQLException {
        String sql = "SELECT * FROM AccessLog";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    // Retrieve all logs as a list of Log objects
    public List<Log> getAllLogsAsList() throws SQLException {
        ResultSet rs = getAllLogs();
        List<Log> logs = new ArrayList<>();
        while (rs.next()) {
            int logID = rs.getInt("LogID");
            String email = rs.getString("Email");
            String action = rs.getString("Action");
            String role = rs.getString("Role");
            String time = rs.getString("Time");
            logs.add(new Log(logID, email, action, role, time));
        }
        return logs;
    }
}