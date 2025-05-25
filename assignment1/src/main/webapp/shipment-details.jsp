<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Shipment, java.time.LocalDate, java.time.format.DateTimeFormatter" %>
<%!
    // Function to format date from YYYY-MM-DD to DD/MM/YYYY
    public String formatDate(String inputDate) {
        try {
            // Parse YYYY-MM-DD 
            LocalDate date = LocalDate.parse(inputDate);
            // Format as DD/MM/YYYY
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            return inputDate;
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipment Details - IoTBay</title>
    <link rel="stylesheet" href="shipment-style.css">
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
            <a href="index.jsp">Home</a>
            <a href="main.jsp">Main</a>
        </div>
        <div class="nav-right">
            <a href="shipment-dashboard.jsp">Shipments</a>
            <a href="payment-dashboard.jsp">Payments</a>
            <a href="logout.jsp">Logout</a>
        </div>
    </nav>

    <div class="details-container">
        <%
            Shipment shipment = (Shipment) request.getAttribute("shipment");
            if (shipment == null) {
        %>
            <h2>Shipment Not Found</h2>
            <p class="not-found">The requested shipment could not be found or you don't have permission to view it.</p>
            <div class="action-buttons">
                <a href="shipment-dashboard.jsp" class="btn-action btn-secondary">Back to Shipments</a>
            </div>
        <%
            } else {
                String statusClass = "";
                if ("Pending".equals(shipment.getStatus())) {
                    statusClass = "status-pending";
                } else if ("Complete".equals(shipment.getStatus())) {
                    statusClass = "status-complete";
                }
        %>
            <h2>Shipment Details</h2>
            
            <div class="details-section">
                <div class="details-row">
                    <div class="details-label">Shipment ID:</div>
                    <div class="details-value"><%= shipment.getShipmentId() %></div>
                </div>
                
                <div class="details-row">
                    <div class="details-label">Order ID:</div>
                    <div class="details-value"><%= shipment.getOrderId() %></div>
                </div>
                
                <div class="details-row">
                    <div class="details-label">Shipment Method:</div>
                    <div class="details-value"><%= shipment.getShipmentMethod() %></div>
                </div>
                
                <div class="details-row">
                    <div class="details-label">Shipment Date:</div>
                    <div class="details-value"><%= formatDate(shipment.getShipmentDate()) %></div>
                </div>
                
                <div class="details-row">
                    <div class="details-label">Shipping Address:</div>
                    <div class="details-value"><%= shipment.getAddress() %></div>
                </div>
                
                <div class="details-row">
                    <div class="details-label">Status:</div>
                    <div class="details-value">
                        <span class="status-badge <%= statusClass %>"><%= shipment.getStatus() %></span>
                    </div>
                </div>
            </div>
            
            <div class="action-buttons">
                <% if ("Pending".equals(shipment.getStatus())) { %>
                    <a href="ShipmentServlet?action=edit&id=<%= shipment.getShipmentId() %>" class="btn-action btn-primary">Edit Shipment</a>
                    <a href="ShipmentServlet?action=delete&id=<%= shipment.getShipmentId() %>" class="btn-action btn-important" 
                       onclick="return confirm('Are you sure you want to delete this shipment?')">Delete Shipment</a>
                    <a href="ShipmentServlet?action=complete&id=<%= shipment.getShipmentId() %>" class="btn-action btn-secondary" 
                       onclick="return confirm('Are you sure you want to mark this shipment as complete? This action cannot be undone.')">Mark as Complete</a>
                <% } %>
                <a href="shipment-dashboard.jsp" class="btn-action btn-secondary">Back to Shipments</a>
            </div>
        <% } %>
    </div>
</body>
</html>