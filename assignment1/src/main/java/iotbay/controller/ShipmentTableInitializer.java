package iotbay.controller;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import iotbay.dao.DBConnector;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShipmentTableInitializer extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ShipmentTableInitializer.class.getName());
    
    @Override
    public void init() throws ServletException {
        try {
            LOGGER.info("Starting Shipment table initialization...");
            
            // Connect to database
            DBConnector connector = new DBConnector();
            Connection conn = connector.openConnection();
            
            // Execute SQL file
            String sqlFilePath = getServletContext().getRealPath("/WEB-INF/database/create_shipment_table.sql");
            LOGGER.info(() -> "SQL file path: " + sqlFilePath);
            
            executeScriptFromFile(conn, sqlFilePath);
            
            // Close connection
            connector.closeConnection();
            
            LOGGER.info(() -> "Shipment table initialization completed successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing Shipment table: " + e.getMessage(), e);
            throw new ServletException("Error initializing Shipment table: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Shipment Table Initializer</h1>");
        response.getWriter().println("<p>The Shipment table has been initialized.</p>");
        response.getWriter().println("<p><a href='shipment-dashboard.jsp'>Go to Shipment Dashboard</a></p>");
        response.getWriter().println("</body></html>");
    }
    
    private void executeScriptFromFile(Connection conn, String filePath) throws SQLException, IOException {
        LOGGER.info(() -> "Reading SQL script from: " + filePath);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder script = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            script.append(line).append("\n");
        }
        
        reader.close();
        
        // Split script into individual statements
        String[] statements = script.toString().split(";");
        
        Statement stmt = conn.createStatement();
        
        for (String statement : statements) {
            if (!statement.trim().isEmpty()) {
                LOGGER.info(() -> "Executing SQL: " + statement.trim());
                stmt.execute(statement);
            }
        }
        
        stmt.close();
        LOGGER.info(() -> "SQL script executed successfully: " + filePath);
    }
}