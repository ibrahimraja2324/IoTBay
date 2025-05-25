<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Shipment, iotbay.model.User, java.time.LocalDate, java.time.format.DateTimeFormatter" %>
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
            <a href="main.jsp">Home</a>
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
            
            // Values to prefill form if there was an error
            String orderId = (String) request.getAttribute("orderId");
            String shipmentMethod = (String) request.getAttribute("shipmentMethod");
            String shipmentDate = (String) request.getAttribute("shipmentDate");
            String address = (String) request.getAttribute("address");
            
            // Get today's date for min attribute
            String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            
            // Clear error message from session after displaying
            session.removeAttribute("shipmentError");
        %>
        
        <h2><%= isEditing ? "Edit Shipment" : "Create New Shipment" %></h2>
        
        <% if (shipmentError != null) { %>
            <div class="error-message">
                <%= shipmentError %>
            </div>
        <% } %>
        
        <form action="ShipmentServlet" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="<%= isEditing ? "update" : "add" %>">
            <% if (isEditing) { %>
                <input type="hidden" name="shipmentId" value="<%= shipment.getShipmentId() %>">
            <% } %>
            
            <div class="form-field">
                <label for="orderId">Order ID:</label>
                <input type="number" id="orderId" name="orderId" min="1" step="1" 
                       value="<%= isEditing ? shipment.getOrderId() : (orderId != null ? orderId : "") %>" required>
                <div id="orderIdError" class="field-error"></div>
            </div>
            
            <div class="form-field">
                <label for="shipmentMethod">Shipment Method:</label>
                <select id="shipmentMethod" name="shipmentMethod" required>
                    <option value="" disabled <%= (!isEditing && shipmentMethod == null) ? "selected" : "" %>>Select a shipment method</option>
                    <option value="Standard Shipping" <%= (isEditing && "Standard Shipping".equals(shipment.getShipmentMethod())) || "Standard Shipping".equals(shipmentMethod) ? "selected" : "" %>>Standard Shipping</option>
                    <option value="Express Shipping" <%= (isEditing && "Express Shipping".equals(shipment.getShipmentMethod())) || "Express Shipping".equals(shipmentMethod) ? "selected" : "" %>>Express Shipping</option>
                    <option value="Priority Shipping" <%= (isEditing && "Priority Shipping".equals(shipment.getShipmentMethod())) || "Priority Shipping".equals(shipmentMethod) ? "selected" : "" %>>Priority Shipping</option>
                    <option value="Next Day Delivery" <%= (isEditing && "Next Day Delivery".equals(shipment.getShipmentMethod())) || "Next Day Delivery".equals(shipmentMethod) ? "selected" : "" %>>Next Day Delivery</option>
                </select>
                <div id="shipmentMethodError" class="field-error"></div>
            </div>
            
            <div class="form-field">
                <label for="shipmentDate">Shipment Date:</label>
                <input type="date" id="shipmentDate" name="shipmentDate" min="<%= today %>"
                       value="<%= isEditing ? shipment.getShipmentDate() : (shipmentDate != null ? shipmentDate : "") %>" required>
                <div id="shipmentDateError" class="field-error"></div>
            </div>
            
            <div class="form-field">
                <label for="address">Shipping Address:</label>
                <input type="text" id="address" name="address" placeholder="Enter your shipping address" 
                       value="<%= isEditing ? shipment.getAddress() : (address != null ? address : "") %>" required>
                <div id="addressError" class="field-error"></div>
            </div>
            
            <div class="form-buttons">
                <button type="submit" class="btn-submit"><%= isEditing ? "Update Shipment" : "Create Shipment" %></button>
                <a href="shipment-dashboard.jsp" class="btn-cancel">Cancel</a>
            </div>
        </form>
    </div>

    <script>
        function validateForm() {
            let isValid = true;
            const orderIdField = document.getElementById('orderId');
            const shipmentMethodField = document.getElementById('shipmentMethod');
            const shipmentDateField = document.getElementById('shipmentDate');
            const addressField = document.getElementById('address');
            
            // Clear previous error messages
            document.getElementById('orderIdError').textContent = '';
            document.getElementById('shipmentMethodError').textContent = '';
            document.getElementById('shipmentDateError').textContent = '';
            document.getElementById('addressError').textContent = '';
            
            // Order ID validation
            const orderId = orderIdField.value.trim();
            if (!orderId) {
                document.getElementById('orderIdError').textContent = 'Order ID is required';
                isValid = false;
            } else {
                const orderIdNum = parseInt(orderId);
                if (isNaN(orderIdNum) || orderIdNum <= 0) {
                    document.getElementById('orderIdError').textContent = 'Order ID must be a positive number';
                    isValid = false;
                }
            }
            
            // Shipment Method validation
            if (!shipmentMethodField.value) {
                document.getElementById('shipmentMethodError').textContent = 'Please select a shipment method';
                isValid = false;
            }
            
            // Shipment Date validation
            if (!shipmentDateField.value) {
                document.getElementById('shipmentDateError').textContent = 'Shipment date is required';
                isValid = false;
            } else {
                const selectedDate = new Date(shipmentDateField.value);
                const today = new Date();
                today.setHours(0, 0, 0, 0); // Reset time to beginning of day for comparison
                
                if (selectedDate < today) {
                    document.getElementById('shipmentDateError').textContent = 'Shipment date cannot be in the past';
                    isValid = false;
                }
            }
            
            // Address validation
            if (!addressField.value.trim()) {
                document.getElementById('addressError').textContent = 'Shipping address is required';
                isValid = false;
            }
            
            return isValid;
        }
    </script>
</body>
</html>