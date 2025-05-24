<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Shipment, iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipment Form - IoTBay</title>
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

    <div class="form-container">
        <%
            Shipment shipment = (Shipment) request.getAttribute("shipment");
            User currentUser = (User) session.getAttribute("currentUser");
            
            boolean isEditing = (shipment != null);
            String shipmentError = (String) session.getAttribute("shipmentError");
            
            // Clear error message from session after displaying
            session.removeAttribute("shipmentError");
        %>
        
        <h2><%= isEditing ? "Edit Shipment" : "Create New Shipment" %></h2>
        
        <% if (shipmentError != null) { %>
            <div class="error-message">
                <%= shipmentError %>
            </div>
        <% } %>
        
        <form action="ShipmentServlet" method="post">
            <input type="hidden" name="action" value="<%= isEditing ? "update" : "add" %>">
            <% if (isEditing) { %>
                <input type="hidden" name="shipmentId" value="<%= shipment.getShipmentId() %>">
            <% } %>
            
            <div class="form-field">
                <label for="orderId">Order ID:</label>
                <input type="number" id="orderId" name="orderId" value="<%= isEditing ? shipment.getOrderId() : "" %>" required>
            </div>
            
            <div class="form-field">
                <label for="shipmentMethod">Shipment Method:</label>
                <select id="shipmentMethod" name="shipmentMethod" required>
                    <option value="" disabled <%= !isEditing ? "selected" : "" %>>Select a shipment method</option>
                    <option value="Standard Shipping" <%= (isEditing && "Standard Shipping".equals(shipment.getShipmentMethod())) ? "selected" : "" %>>Standard Shipping</option>
                    <option value="Express Shipping" <%= (isEditing && "Express Shipping".equals(shipment.getShipmentMethod())) ? "selected" : "" %>>Express Shipping</option>
                    <option value="Priority Shipping" <%= (isEditing && "Priority Shipping".equals(shipment.getShipmentMethod())) ? "selected" : "" %>>Priority Shipping</option>
                    <option value="Next Day Delivery" <%= (isEditing && "Next Day Delivery".equals(shipment.getShipmentMethod())) ? "selected" : "" %>>Next Day Delivery</option>
                </select>
            </div>
            
            <div class="form-field">
                <label for="shipmentDate">Shipment Date:</label>
                <input type="date" id="shipmentDate" name="shipmentDate" value="<%= isEditing ? shipment.getShipmentDate() : "" %>" required>
            </div>
            
            <div class="form-field">
                <label for="address">Shipping Address:</label>
                <input type="text" id="address" name="address" placeholder="Enter your shipping address" 
                       value="<%= isEditing ? shipment.getAddress() : "" %>" required>
            </div>
            
            <div class="form-buttons">
                <button type="submit" class="btn-submit"><%= isEditing ? "Update Shipment" : "Create Shipment" %></button>
                <a href="shipment-dashboard.jsp" class="btn-cancel">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>