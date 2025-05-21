<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Shipment" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipment Details - IoTBay</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .details-container {
            max-width: 600px;
            margin: 120px auto 50px auto;
            background-color: #2c2a3a;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }
        
        .details-container h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #fff;
        }
        
        .details-section {
            margin-bottom: 30px;
        }
        
        .details-row {
            display: flex;
            margin-bottom: 15px;
        }
        
        .details-label {
            flex: 1;
            font-weight: 600;
            color: #a6a6ff;
        }
        
        .details-value {
            flex: 2;
            color: #fff;
        }
        
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
        
        .action-buttons {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 30px;
        }
        
        .btn-primary, .btn-secondary, .btn-important {
            padding: 12px 24px;
            border-radius: 5px;
            text-decoration: none;
            font-size: 16px;
            text-align: center;
        }
        
        .btn-primary {
            background: linear-gradient(to right, #2575fc, #6a11cb);
            color: #fff;
        }
        
        .btn-primary:hover {
            background: linear-gradient(to right, #6a11cb, #2575fc);
        }
        
        .btn-secondary {
            background: #ccc;
            color: #333;
        }
        
        .btn-secondary:hover {
            background: #bbb;
        }
        
        .btn-important {
            background: linear-gradient(to right, #e52d27, #b31217);
            color: #fff;
        }
        
        .btn-important:hover {
            background: linear-gradient(to right, #b31217, #e52d27);
        }
    </style>
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
            <a href="index.jsp">Home</a>
            <a href="main.jsp">Main</a>
        </div>
        <div class="nav-right">
            <a href="shipment-dashboard.jsp">Shipments</a>
            <a href="logout.jsp">Logout</a>
        </div>
    </nav>

    <div class="details-container">
        <%
            Shipment shipment = (Shipment) request.getAttribute("shipment");
            if (shipment == null) {
        %>
            <h2>Shipment Not Found</h2>
            <p style="text-align: center; color: #ff6b6b;">The requested shipment could not be found.</p>
            <div class="action-buttons">
                <a href="shipment-dashboard.jsp" class="btn-secondary">Back to Shipments</a>
            </div>
        <%
            } else {
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
                    <div class="details-value"><%= shipment.getShipmentDate() %></div>
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
                    <a href="ShipmentServlet?action=edit&id=<%= shipment.getShipmentId() %>" class="btn-primary">Edit Shipment</a>
                    <a href="ShipmentServlet?action=delete&id=<%= shipment.getShipmentId() %>" class="btn-important" 
                       onclick="return confirm('Are you sure you want to delete this shipment?')">Delete Shipment</a>
                <% } %>
                <a href="shipment-dashboard.jsp" class="btn-secondary">Back to Shipments</a>
            </div>
        <% } %>
    </div>
</body>
</html>