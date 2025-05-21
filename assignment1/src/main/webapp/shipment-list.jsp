<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Shipment" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shipment List</title>
    <style>
        body {
            font-family: 'Quicksand', sans-serif;
            background-color: #1f1c2c;
            color: #fff;
            margin: 0;
            padding: 20px;
        }
        h1 {
            color: #fff;
            text-align: center;
            margin-bottom: 20px;
        }
        table { 
            border-collapse: collapse; 
            width: 100%;
            margin: 0 auto;
            background-color: #28243c;
            border-radius: 8px;
            overflow: hidden;
        }
        table, th, td { 
            border: 1px solid #35325a; 
        }
        th, td { 
            padding: 12px; 
            text-align: center; 
        }
        th { 
            background-color: #22203a;
            color: #a6a6ff;
            font-weight: 600;
        }
        tr:hover td {
            background-color: #23203a;
        }
        .action-link {
            color: #4dabf7;
            text-decoration: none;
            margin: 0 5px;
        }
        .action-link:hover {
            text-decoration: underline;
        }
        .status-pending {
            background-color: #ffc107;
            padding: 4px 8px;
            border-radius: 4px;
            color: #000;
        }
        .status-processing {
            background-color: #17a2b8;
            padding: 4px 8px;
            border-radius: 4px;
            color: #fff;
        }
        .status-shipped {
            background-color: #28a745;
            padding: 4px 8px;
            border-radius: 4px;
            color: #fff;
        }
        .status-delivered {
            background-color: #6c757d;
            padding: 4px 8px;
            border-radius: 4px;
            color: #fff;
        }
        .no-records {
            text-align: center;
            padding: 20px;
            font-style: italic;
            color: #ccc;
        }
        .search-info {
            text-align: center;
            margin-bottom: 15px;
            font-style: italic;
            color: #ccc;
        }
    </style>
</head>
<body>
    <h1>Your Shipments</h1>
    
    <% 
        String searchTerm = (String) request.getAttribute("searchTerm");
        String searchBy = (String) request.getAttribute("searchBy");
        
        if (searchTerm != null && !searchTerm.isEmpty()) {
    %>
    <div class="search-info">
        Search results for <%= searchBy.equals("id") ? "Shipment ID" : "Shipment Date" %>: <%= searchTerm %>
    </div>
    <% } %>
    
    <%
        List<Shipment> shipments = (List<Shipment>) request.getAttribute("shipments");
        if (shipments == null || shipments.isEmpty()) {
    %>
        <div class="no-records">No shipments found.</div>
    <%
        } else {
    %>
        <table>
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
                    <td><span class="<%= statusClass %>"><%= shipment.getStatus() %></span></td>
                    <td>
                        <a href="ShipmentServlet?action=view&id=<%= shipment.getShipmentId() %>" class="action-link">View</a>
                        <% if ("Pending".equals(shipment.getStatus())) { %>
                            | <a href="ShipmentServlet?action=edit&id=<%= shipment.getShipmentId() %>" class="action-link">Edit</a>
                            | <a href="ShipmentServlet?action=delete&id=<%= shipment.getShipmentId() %>" class="action-link" onclick="return confirm('Are you sure you want to delete this shipment?')">Delete</a>
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