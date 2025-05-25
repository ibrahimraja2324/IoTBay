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
            max-width: 500px;
            margin: 120px auto 50px auto;
            background-color: #28243c;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }
        
        .details-container h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #a6a6ff;
            font-weight: 600;
        }
        
        .details-section {
            margin-bottom: 30px;
        }
        
        .details-row {
            display: flex;
            margin-bottom: 18px;
            border-bottom: 1px solid #35325a;
            padding-bottom: 12px;
        }
        
        .details-row:last-child {
            border-bottom: none;
        }
        
        .details-label {
            flex: 1;
            font-weight: 600;
            color: #b2b2b2;
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
            flex-wrap: wrap;
            justify-content: center;
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn-action {
            padding: 12px 24px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 16px;
            text-align: center;
            min-width: 140px;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background: linear-gradient(to right, #2575fc, #6a11cb);
            color: #fff;
        }
        
        .btn-primary:hover {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(37, 117, 252, 0.3);
        }
        
        .btn-secondary {
            background: #35325a;
            color: #fff;
        }
        
        .btn-secondary:hover {
            background: #413d6b;
            transform: translateY(-2px);
        }
        
        .btn-important {
            background: linear-gradient(to right, #e52d27, #b31217);
            color: #fff;
        }
        
        .btn-important:hover {
            background: linear-gradient(to right, #b31217, #e52d27);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(229, 45, 39, 0.3);
        }
        
        .not-found {
            text-align: center;
            color: #ff6b6b;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
            <a href="main.jsp">Home</a>

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
                    <a href="ShipmentServlet?action=edit&id=<%= shipment.getShipmentId() %>" class="btn-action btn-primary">Edit Shipment</a>
                    <a href="ShipmentServlet?action=delete&id=<%= shipment.getShipmentId() %>" class="btn-action btn-important" 
                       onclick="return confirm('Are you sure you want to delete this shipment?')">Delete Shipment</a>
                <% } %>
                <a href="shipment-dashboard.jsp" class="btn-action btn-secondary">Back to Shipments</a>
            </div>
        <% } %>
    </div>
</body>
</html>