<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Shipment, iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipment Form - IoTBay</title>
    <link rel="stylesheet" href="style.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 120px auto 50px auto;
            background-color: #2c2a3a;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }
        
        .form-container h2 {
            text-align: center;
            margin-bottom: 25px;
            color: #fff;
        }
        
        .form-field {
            margin-bottom: 20px;
        }
        
        .form-field label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #b2b2b2;
        }
        
        .form-field input, .form-field select {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 4px;
            background-color: #333;
            color: #fff;
            font-family: 'Quicksand', sans-serif;
            font-size: 16px;
        }
        
        .form-field input:focus, .form-field select:focus {
            outline: none;
            box-shadow: 0 0 0 2px #2575fc;
        }
        
        .form-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: 30px;
        }
        
        .btn-submit {
            padding: 12px 24px;
            background: linear-gradient(to right, #2575fc, #6a11cb);
            color: white;
            border: none;
            border-radius: 4px;
            font-family: 'Quicksand', sans-serif;
            font-size: 16px;
            cursor: pointer;
            flex-grow: 1;
            margin-right: 10px;
        }
        
        .btn-submit:hover {
            background: linear-gradient(to right, #6a11cb, #2575fc);
        }
        
        .btn-cancel {
            padding: 12px 24px;
            background: #ccc;
            color: #333;
            border: none;
            border-radius: 4px;
            font-family: 'Quicksand', sans-serif;
            font-size: 16px;
            text-decoration: none;
            text-align: center;
            cursor: pointer;
            flex-grow: 1;
            margin-left: 10px;
        }
        
        .btn-cancel:hover {
            background: #bbb;
        }
        
        .error-message {
            color: #ff6b6b;
            margin-top: 5px;
            font-size: 14px;
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
            <div class="error-message" style="text-align: center; margin-bottom: 15px;">
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