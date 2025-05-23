<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="iotbay.model.Device" %>
<!DOCTYPE html>
<html>
<head>
    <title>Products</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #1a5ac7;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        h1 {
            text-align: center;
            color: #28a745;
            margin-top: 20px;
        }
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            padding: 20px;
            max-width: 1200px;
            width: 100%;
        }
        .card {
            border: 1px solid #ccc;
            border-radius: 10px;
            padding: 16px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            background-color: #ffffff;
            text-align: center;
        }
        .card h3 {
            margin-top: 0;
        }
        .card p {
            margin: 4px 0;
        }
        .buy-button {
            margin-top: 10px;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .buy-button:hover {
            background-color: #218838;
        }
    </style>
    <script>
        function buyDevice(name) {
            alert("You have successfully bought " + name + "!");
        }
    </script>
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
          <a href="index.jsp">Home</a>
        </div>
        <div class="nav-right">
          <a href="logout.jsp">Logout</a>
          <a href="payment-dashboard.jsp">Manage Payments</a>
          <a href="shipment-dashboard.jsp">Manage Shipments</a>
        </div>
      </nav>
    <h1>Available Devices</h1>
    <div class="grid">
        <%
            List<Device> list = (List<Device>) request.getAttribute("devices");
            for (Device d : list) {
        %>
        <div class="card">
            <h3><%= d.getName() %></h3>
            <p><strong>Type:</strong> <%= d.getType() %></p>
            <p><strong>Price:</strong> $<%= d.getUnitPrice() %></p>
            <p><strong>Quantity:</strong> <%= d.getQuantity() %></p>
            <p><small>ID: <%= d.getId() %></small></p>
            <button class="buy-button" onclick="buyDevice('<%= d.getName() %>')">Buy Now</button>
        </div>
        <%
            }
        %>
    </div>
</body>
</html>
