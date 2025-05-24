<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Shipment" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shipment List</title>
    <link rel="stylesheet" href="shipment-style.css">
</head>
<body>
    <% 
        String searchTerm = (String) request.getAttribute("searchTerm");
        String searchBy = (String) request.getAttribute("searchBy");
        
        if (searchTerm != null && !searchTerm.isEmpty()) {
    %>
    <div class="search-info">
        Search results for <%= searchBy.equals("id") ? "Shipment ID" : "Shipment Date" %>: "<%= searchTerm %>"
    </div>
    <% } %>
    
    <%
        List<Shipment> shipments = (List<Shipment>) request.getAttribute("shipments");
        if (shipments == null || shipments.isEmpty()) {
    %>
        <div class="no-records">No shipments found. Create a new shipment to get started!</div>
    <%
        } else {
    %>
        <table class="shipment-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Order ID</th>
                    <th>Shipment Method</th>
                    <th>Shipment Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Shipment shipment : shipments) {
                        String statusClass = "";
                        switch (shipment.getStatus().toLowerCase()) {
                            case "pending":
                                statusClass = "status-pending";
                                break;
                            case "processing":
                                statusClass = "status-processing";
                                break;
                            case "shipped":
                                statusClass = "status-shipped";
                                break;
                            case "delivered":
                                statusClass = "status-delivered";
                                break;
                            default:
                                statusClass = "";
                        }
                %>
                <tr>
                    <td><%= shipment.getShipmentId() %></td>
                    <td><%= shipment.getOrderId() %></td>
                    <td><%= shipment.getShipmentMethod() %></td>
                    <td><%= shipment.getShipmentDate() %></td>
                    <td><span class="status-badge <%= statusClass %>"><%= shipment.getStatus() %></span></td>
                    <td>
                        <a href="ShipmentServlet?action=view&id=<%= shipment.getShipmentId() %>" class="action-link" target="_top">View</a>
                        <% if ("Pending".equals(shipment.getStatus())) { %>
                            | <a href="ShipmentServlet?action=edit&id=<%= shipment.getShipmentId() %>" class="action-link" target="_top">Edit</a>
                            | <a href="ShipmentServlet?action=delete&id=<%= shipment.getShipmentId() %>" class="action-link" 
                                 onclick="return confirm('Are you sure you want to delete this shipment?')" target="_top">Delete</a>
                        <% } %>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        }
    %>
</body>
</html>