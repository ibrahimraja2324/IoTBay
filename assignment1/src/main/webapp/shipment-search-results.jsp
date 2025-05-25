<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Shipment, iotbay.model.User, java.time.LocalDate, java.time.format.DateTimeFormatter" %>
<%!
    // Function to format date from YYYY-MM-DD to DD/MM/YYYY
    public String formatDate(String inputDate) {
        try {
            // Parse the input date (assuming YYYY-MM-DD format)
            LocalDate date = LocalDate.parse(inputDate);
            // Format as DD/MM/YYYY
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            // Return original if there's any error
            return inputDate;
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipment Search Results - IoTBay</title>
    <link rel="stylesheet" href="shipment-style.css">
</head>
<body>
    <!-- Navigation Menu -->
    <nav class="page-nav">
        <div class="nav-left">
            <a href="main.jsp">Home</a>
        </div>
        <div class="nav-right">
            <a href="shipment-dashboard.jsp">Shipments</a>
            <a href="payment-dashboard.jsp">Payments</a>
            <a href="edit.jsp">Profile</a>
            <a href="LogoutServlet">Logout</a>
        </div>
    </nav>
    
    <div class="container">
        <h1 class="page-title">Shipment Search Results</h1>
        
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
        
        <div class="search-container">
            <form action="ShipmentServlet" method="get" class="search-form">
                <input type="hidden" name="action" value="search">
                <select name="searchBy" id="searchBy" onchange="toggleSearchInput()">
                    <option value="id" <%= (searchBy != null && searchBy.equals("id")) ? "selected" : "" %>>Shipment ID</option>
                    <option value="date" <%= (searchBy != null && searchBy.equals("date")) ? "selected" : "" %>>Shipment Date</option>
                </select>
                
                <input type="text" name="searchTerm" id="textSearch" placeholder="Enter Shipment ID..." 
                       value="<%= (searchBy != null && searchBy.equals("id") && searchTerm != null) ? searchTerm : "" %>" 
                       class="<%= (searchBy != null && searchBy.equals("date")) ? "d-none" : "" %>">
                
                <input type="date" name="searchTerm" id="dateSearch" 
                       value="<%= (searchBy != null && searchBy.equals("date") && searchTerm != null) ? searchTerm : "" %>"
                       class="<%= (searchBy == null || !searchBy.equals("date")) ? "d-none" : "" %>">
                
                <button type="submit">Search</button>
            </form>
        </div>
        
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
                            if ("Pending".equals(shipment.getStatus())) {
                                statusClass = "status-pending";
                            } else if ("Complete".equals(shipment.getStatus())) {
                                statusClass = "status-complete";
                            }
                    %>
                    <tr>
                        <td><%= shipment.getShipmentId() %></td>
                        <td><%= shipment.getOrderId() %></td>
                        <td><%= shipment.getShipmentMethod() %></td>
                        <td><%= formatDate(shipment.getShipmentDate()) %></td>
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

    <script>
        function toggleSearchInput() {
            var searchBy = document.getElementById('searchBy').value;
            var textSearch = document.getElementById('textSearch');
            var dateSearch = document.getElementById('dateSearch');
            
            if (searchBy === 'date') {
                textSearch.classList.add('d-none');
                dateSearch.classList.remove('d-none');
                textSearch.name = '';
                dateSearch.name = 'searchTerm';
            } else {
                textSearch.classList.remove('d-none');
                dateSearch.classList.add('d-none');
                textSearch.name = 'searchTerm';
                dateSearch.name = '';
            }
        }

        // Initialize the form on page load
        document.addEventListener('DOMContentLoaded', function() {
            toggleSearchInput();
        });
    </script>
</body>
</html>