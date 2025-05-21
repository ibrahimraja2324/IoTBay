package iotbay.controller;

import iotbay.dao.DBManager;
import iotbay.dao.LogDAO;
import iotbay.dao.ShipmentDAO;
import iotbay.model.Log;
import iotbay.model.Shipment;
import iotbay.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShipmentServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ShipmentServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        
        if (manager == null) {
            try {
                throw new ServletException("DBManager not initialized. Please navigate from the home page.");
            } catch (ServletException e) {
                LOGGER.log(Level.SEVERE, "DBManager not initialized.", e);
                response.sendRedirect("error.jsp");
                return;
            }
        }
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "create":
                    try {
                        showCreateForm(request, response);
                    } catch (ServletException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Error showing create form", e);
                        throw new ServletException("Error showing create form", e);
                    }
                    break;
                case "edit":
                    try {
                        showEditForm(request, response, manager);
                    } catch (ServletException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Error showing edit form", e);
                        throw new ServletException("Error showing edit form", e);
                    }
                    break;
                case "delete":
                    try {
                        deleteShipment(request, response, manager);
                    } catch (ServletException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Error deleting shipment", e);
                        throw new ServletException("Error deleting shipment", e);
                    }
                    break;
                case "view":
                    try {
                        viewShipment(request, response, manager);
                    } catch (ServletException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Error viewing shipment", e);
                        throw new ServletException("Error viewing shipment", e);
                    }
                    break;
                case "search":
                    try {
                        searchShipments(request, response, manager);
                    } catch (ServletException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Error searching shipments", e);
                        throw new ServletException("Error searching shipments", e);
                    }
                    break;
                default:
                    try {
                        listShipments(request, response, manager);
                    } catch (ServletException | IOException e) {
                        LOGGER.log(Level.SEVERE, "Error listing shipments", e);
                        throw new ServletException("Error listing shipments", e);
                    }
                    break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error in ShipmentServlet", ex);
            throw new ServletException(ex);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        DBManager manager = (DBManager) session.getAttribute("manager");
        
        if (manager == null) {
            throw new ServletException("DBManager not initialized. Please navigate from the home page.");
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "add":
                    addShipment(request, response, manager);
                    break;
                case "update":
                    updateShipment(request, response, manager);
                    break;
                default:
                    response.sendRedirect("shipment-dashboard.jsp");
                    break;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error in ShipmentServlet", ex);
            throw new ServletException(ex);
        }
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Just forward to the shipment form page
        request.getRequestDispatcher("shipment-form.jsp").forward(request, response);
    }
    
    private void addShipment(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Retrieve form parameters
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String shipmentMethod = request.getParameter("shipmentMethod");
        String shipmentDate = request.getParameter("shipmentDate");
        String address = request.getParameter("address");
        
        // Validation - Basic example, expand as needed
        boolean hasError = false;
        
        if (shipmentMethod == null || shipmentMethod.trim().isEmpty()) {
            session.setAttribute("shipmentError", "Shipment method is required");
            hasError = true;
        }
        
        if (shipmentDate == null || shipmentDate.trim().isEmpty()) {
            session.setAttribute("shipmentError", "Shipment date is required");
            hasError = true;
        }
        
        if (address == null || address.trim().isEmpty()) {
            session.setAttribute("shipmentError", "Address is required");
            hasError = true;
        }
        
        if (hasError) {
            response.sendRedirect("ShipmentServlet?action=create");
            return;
        }
        
        // Create new shipment
        Shipment shipment = new Shipment();
        shipment.setOrderId(orderId);
        shipment.setShipmentMethod(shipmentMethod);
        shipment.setShipmentDate(shipmentDate);
        shipment.setAddress(address);
        shipment.setStatus("Pending"); // Default status for new shipments
        shipment.setUserEmail(currentUser.getEmail());
        
        // Save to database
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        boolean isCreated = shipmentDAO.createShipment(shipment);
        
        if (isCreated) {
            // Log the creation
            Log log = new Log(currentUser.getEmail(), "Created shipment", currentUser.getRole());
            LogDAO logDAO = new LogDAO(conn);
            logDAO.createLog(log);
            
            session.setAttribute("successMessage", "Shipment created successfully");
        } else {
            session.setAttribute("shipmentError", "Failed to create shipment");
        }
        
        response.sendRedirect("shipment-dashboard.jsp");
    }
    
    private void listShipments(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get all shipments for the current user
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        List<Shipment> shipments = shipmentDAO.findShipmentsByUser(currentUser.getEmail());
        
        request.setAttribute("shipments", shipments);
        request.getRequestDispatcher("shipment-list.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get the shipment ID from the request
        int shipmentId = Integer.parseInt(request.getParameter("id"));
        
        // Get the shipment from the database
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        Shipment shipment = shipmentDAO.findShipment(shipmentId);
        
        // Check if the shipment exists and belongs to the current user
        if (shipment == null || !shipment.getUserEmail().equals(currentUser.getEmail())) {
            session.setAttribute("shipmentError", "Shipment not found or you do not have permission to edit it");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        // Check if the shipment is finalized (not in pending status)
        if (!"Pending".equals(shipment.getStatus())) {
            session.setAttribute("shipmentError", "Cannot edit a shipment that has been finalized");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        request.setAttribute("shipment", shipment);
        request.getRequestDispatcher("shipment-form.jsp").forward(request, response);
    }
    
    private void updateShipment(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Retrieve form parameters
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String shipmentMethod = request.getParameter("shipmentMethod");
        String shipmentDate = request.getParameter("shipmentDate");
        String address = request.getParameter("address");
        
        // Validation - Basic example, expand as needed
        boolean hasError = false;
        
        if (shipmentMethod == null || shipmentMethod.trim().isEmpty()) {
            session.setAttribute("shipmentError", "Shipment method is required");
            hasError = true;
        }
        
        if (shipmentDate == null || shipmentDate.trim().isEmpty()) {
            session.setAttribute("shipmentError", "Shipment date is required");
            hasError = true;
        }
        
        if (address == null || address.trim().isEmpty()) {
            session.setAttribute("shipmentError", "Address is required");
            hasError = true;
        }
        
        if (hasError) {
            response.sendRedirect("ShipmentServlet?action=edit&id=" + shipmentId);
            return;
        }
        
        // Get the existing shipment
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        Shipment existingShipment = shipmentDAO.findShipment(shipmentId);
        
        // Check if the shipment exists and belongs to the current user
        if (existingShipment == null || !existingShipment.getUserEmail().equals(currentUser.getEmail())) {
            session.setAttribute("shipmentError", "Shipment not found or you do not have permission to edit it");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        // Check if the shipment is finalized
        if (!"Pending".equals(existingShipment.getStatus())) {
            session.setAttribute("shipmentError", "Cannot edit a shipment that has been finalized");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        // Update the shipment
        existingShipment.setOrderId(orderId);
        existingShipment.setShipmentMethod(shipmentMethod);
        existingShipment.setShipmentDate(shipmentDate);
        existingShipment.setAddress(address);
        
        boolean isUpdated = shipmentDAO.updateShipment(existingShipment);
        
        if (isUpdated) {
            // Log the update
            Log log = new Log(currentUser.getEmail(), "Updated shipment", currentUser.getRole());
            LogDAO logDAO = new LogDAO(conn);
            logDAO.createLog(log);
            
            session.setAttribute("successMessage", "Shipment updated successfully");
        } else {
            session.setAttribute("shipmentError", "Failed to update shipment");
        }
        
        response.sendRedirect("shipment-dashboard.jsp");
    }
    
    private void deleteShipment(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get the shipment ID from the request
        int shipmentId = Integer.parseInt(request.getParameter("id"));
        
        // Get the shipment from the database
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        Shipment shipment = shipmentDAO.findShipment(shipmentId);
        
        // Check if the shipment exists and belongs to the current user
        if (shipment == null || !shipment.getUserEmail().equals(currentUser.getEmail())) {
            session.setAttribute("shipmentError", "Shipment not found or you do not have permission to delete it");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        // Check if the shipment is finalized
        if (!"Pending".equals(shipment.getStatus())) {
            session.setAttribute("shipmentError", "Cannot delete a shipment that has been finalized");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        // Delete the shipment
        boolean isDeleted = shipmentDAO.deleteShipment(shipmentId);
        
        if (isDeleted) {
            // Log the deletion
            Log log = new Log(currentUser.getEmail(), "Deleted shipment", currentUser.getRole());
            LogDAO logDAO = new LogDAO(conn);
            logDAO.createLog(log);
            
            session.setAttribute("successMessage", "Shipment deleted successfully");
        } else {
            session.setAttribute("shipmentError", "Failed to delete shipment");
        }
        
        response.sendRedirect("shipment-dashboard.jsp");
    }
    
    private void viewShipment(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get the shipment ID from the request
        int shipmentId = Integer.parseInt(request.getParameter("id"));
        
        // Get the shipment from the database
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        Shipment shipment = shipmentDAO.findShipment(shipmentId);
        
        // Check if the shipment exists and belongs to the current user
        if (shipment == null || !shipment.getUserEmail().equals(currentUser.getEmail())) {
            session.setAttribute("shipmentError", "Shipment not found or you do not have permission to view it");
            response.sendRedirect("shipment-dashboard.jsp");
            return;
        }
        
        request.setAttribute("shipment", shipment);
        request.getRequestDispatcher("shipment-details.jsp").forward(request, response);
    }
    
    private void searchShipments(HttpServletRequest request, HttpServletResponse response, DBManager manager) 
            throws ServletException, IOException, SQLException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get search parameters
        String searchTerm = request.getParameter("searchTerm");
        String searchBy = request.getParameter("searchBy");
        
        // Perform search
        Connection conn = manager.getConnection();
        ShipmentDAO shipmentDAO = new ShipmentDAO(conn);
        List<Shipment> shipments = shipmentDAO.searchShipments(currentUser.getEmail(), searchTerm, searchBy);
        
        // Store results in request attribute and forward to list page
        request.setAttribute("shipments", shipments);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("searchBy", searchBy);
        request.getRequestDispatcher("shipment-list.jsp").forward(request, response);
    }
}