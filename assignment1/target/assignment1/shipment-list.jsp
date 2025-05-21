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
            color: #a6a6ff;
            text-align: center;
            margin-bottom: 20px;
            font-weight: 600;
        }
        
        table { 
            border-collapse: collapse; 
            width: 100%;
            margin: 0 auto;
            background-color: #28243c;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 16px rgba(30, 22, 54, 0.25);
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
            background-color: #35325a;
        }
        
        .action-link {
            color: #4dabf7;
            text-decoration: none;
            margin: 0 5px;
            transition: color 0.2s;
            font-weight: 500;
        }
        
        .action-link:hover {
            color: #83c9ff;
            text-decoration: underline;
        }
        
        /* Status indicators */
        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 4px;
            font-weight: 500;
        }
        
        .status-pending {
            background-color: #ffc107;
            color: #000;
        }
        
        .status-processing {
            background-color: #17a2b8;
            color: #fff;
        }
        
        .status-shipped {
            background-color: #28a745;
            color: #fff;
        }
        
        .status-delivered {
            background-color: #6c757d;
            color: #fff;
        }
        
        .no-records {
            text-align: center;
            padding: 30px;
            font-style: italic;
            color: #b2b2b2;
            background: #28243c;
            border-radius: 8px;
            box-shadow: 0 2px 16px rgba(30, 22, 54, 0.25);
        }
        
        .search-info {
            text-align: center;
            margin-bottom: 20px;
            font-style: italic;
            color: #b2b2b2;
            background-color: #28243c;
            padding: 10px;
            border-radius: 8px;
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