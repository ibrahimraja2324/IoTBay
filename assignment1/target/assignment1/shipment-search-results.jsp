<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Shipment, iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipment Search Results - IoTBay</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .container {
            max-width: 1000px;
            margin: 100px auto 50px auto;
            padding: 0 20px;
        }
        
        .page-title {
            text-align: center;
            color: #fff;
            margin-bottom: 30px;
        }
        
        .card {
            background-color: #28243c;
            border-radius: 8px;
            padding: 25px;
            margin-bottom: 30px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
        }
        
        .search-info {
            text-align: center;
            margin-bottom: 20px;
            padding: 10px 15px;
            background-color: #35325a;
            border-radius: 6px;
            color: #b2b2b2;
        }
        
        .shipment-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            border-radius: 8px;
            overflow: hidden;
        }
        
        .shipment-table th, 
        .shipment-table td {
            padding: 15px;
            border: 1px solid #35325a;
            text-align: center;
        }
        
        .shipment-table th {
            background-color: #22203a;
            color: #a6a6ff;
            font-weight: 600;
        }
        
        .shipment-table tr:hover td {
            background-color: #35325a;
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
        
        .no-records {
            text-align: center;
            padding: 40px 20px;
            font-style: italic;
            color: #b2b2b2;
            background: #28243c;
            border-radius: 8px;
            margin: 20px 0;
        }
        
        .back-link {
            display: inline-block;
            padding: 12px 24px;
            background: linear-gradient(to right, #2575fc, #6a11cb);
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 500;
            margin-top: 20px;
            transition: all 0.3s ease;
        }
        
        .back-link:hover {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(37, 117, 252, 0.3);
        }
        
        .action-container {
            text-align: center;
            margin-top: 20px;
        }
        
        .search-form {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 10px;
            margin: 20px 0;
        }
        
        .search-form select,
        .search-form input[type="text"] {
            padding: 10px;
            border: none;
            border-radius: 4px;
            background-color: #3a3760;
            color: #fff;
        }
        
        .search-form button {
            background: linear-gradient(to right, #2575fc, #6a11cb);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <!-- Navigation Menu -->
    <nav class="page-nav">
        <div class="nav-left">
            <a href="index.jsp">Home</a>
            <a href="main.jsp">Main</a>
        </div>
        <div class="nav-right">
            <a href="payment-dashboard.jsp">Payments</a>
            <a href="edit.jsp">Profile</a>
            <a href="logout.jsp">Logout</a>
        </div>
    </nav>
    
    <div class="container">
        <h1 class="page-title">Shipment Search Results</h1>
        
        <div class="card">
            <% 
                String searchTerm = (String) request.getAttribute("searchTerm");
                String searchBy = (String) request.getAttribute("searchBy");
                
                if (searchTerm != null && !searchTerm.isEmpty()) {
            %>
                <div class="search-info">
                    <% if (searchBy.equals("id")) { %>
                        Showing results for Shipment ID: "<%= searchTerm %>"
                    <% } else { %>
                        Showing results for Shipment Date: "<%= searchTerm %>"
                    <% } %>
                </div>
            <% } %>
            
            <!-- New Search Form -->
            <form action="ShipmentServlet" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <select name="searchBy">
                    <option value="id" <%= (searchBy != null && searchBy.equals("id")) ? "selected" : "" %>>Shipment ID</option>
                    <option value="date" <%= (searchBy != null && searchBy.equals("date")) ? "selected" : "" %>>Shipment Date</option>
                </select>
                <input type="text" name="searchTerm" placeholder="Search term..." value="<%= searchTerm != null ? searchTerm : "" %>">
                <button type="submit">Search</button>
            </form>
            
            <%
                List<Shipment> shipments = (List<Shipment>) request.getAttribute("shipments");
                if (shipments == null || shipments.isEmpty()) {
            %>
                <div class="no-records">
                    <h3>No shipments found</h3>
                    <p>Try a different search term or create a new shipment.</p>
                </div>
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
                                <a href="ShipmentServlet?action=view&id=<%= shipment.getShipmentId() %>" class="action-link">View</a>
                                <% if ("Pending".equals(shipment.getStatus())) { %>
                                    | <a href="ShipmentServlet?action=edit&id=<%= shipment.getShipmentId() %>" class="action-link">Edit</a>
                                    | <a href="ShipmentServlet?action=delete&id=<%= shipment.getShipmentId() %>" class="action-link" 
                                         onclick="return confirm('Are you sure you want to delete this shipment?')">Delete</a>
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
            
            <div class="action-container">
                <a href="shipment-dashboard.jsp" class="back-link">Back to Shipment Management</a>
            </div>
        </div>
    </div>
</body>
</html>