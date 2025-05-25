<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Order" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    Order order = (Order) request.getAttribute("order");
    String message = (String) request.getAttribute("message");
    String dateFormatted = "";
    if (order != null && order.getOrderDate() != null) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormatted = formatter.format(order.getOrderDate());
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Details – IoTBay</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .details-box { max-width: 500px; margin: 40px auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px #0001; padding: 32px; }
        .details-box h2 { margin-bottom: 24px; }
        .details-table { width: 100%; border-collapse: collapse; }
        .details-table th, .details-table td { text-align: left; padding: 8px 12px; }
        .details-table th { background: #f5f5f5; }
        .details-table tr:nth-child(even) { background: #fafafa; }
        .btn { margin-top: 24px; display: inline-block; padding: 8px 16px; text-decoration: none; border-radius: 4px; }
        .btn.primary { background-color: #007BFF; color: white; border: none; }
        .btn.secondary { background-color: #6c757d; color: white; border: none; }
        .alert { background:#ffe0e0; color:#a00; padding:12px 20px; margin-bottom:20px; border-radius:6px; text-align:center; }
    </style>
</head>
<body class="gradient-bg">

<!-- Nav -->
<nav class="page-nav">
    <div class="nav-left">
        <a href="index.jsp">Home</a>
    </div>
    <div class="nav-right">
        <a href="logout.jsp">Logout</a>
    </div>
</nav>

<% if (message != null && !message.isEmpty()) { %>
    <div class="alert"><%= message %></div>
<% } %>

<div class="details-box">
    <h2>Order Details</h2>

    <% if (order != null) { %>
    <table class="details-table">
        <tr><th>Order ID</th><td><%= order.getOrderId() %></td></tr>
        <tr><th>Date</th><td><%= dateFormatted %></td></tr>
        <tr><th>Status</th><td><%= order.getStatus() %></td></tr>
        <tr><th>Delivery Address</th><td><%= order.getDeliveryAddress() %></td></tr>
        <tr><th>Total Price</th><td>$<%= order.getTotalPrice() %></td></tr>
        <tr><th>Quantity</th><td><%= order.getQuantity() %></td></tr>
    </table>

    <form action="updateOrderStatus" method="post" style="margin-top:32px;">
        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
        <label for="status">Update Status:</label>
        <select name="status" id="status">
            <option value="Pending" <%= "Pending".equals(order.getStatus()) ? "selected" : "" %>>Pending</option>
            <option value="Shipped" <%= "Shipped".equals(order.getStatus()) ? "selected" : "" %>>Shipped</option>
            <option value="Delivered" <%= "Delivered".equals(order.getStatus()) ? "selected" : "" %>>Delivered</option>
            <option value="Cancelled" <%= "Cancelled".equals(order.getStatus()) ? "selected" : "" %>>Cancelled</option>
        </select>
        <button class="btn primary" type="submit">Update Status</button>
    </form>
    <a class="btn secondary" href="listOrders">Back to Order List</a>
    <% } else { %>
        <p style="text-align:center; color:red;">No order found or you do not have permission to view this order.</p>
    <% } %>
</div>

</body>
</html>