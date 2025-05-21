<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Order" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order History</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1 style="text-align:center;">Order History</h1>
    <%
        List orders = (List) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
    %>
        <p style="text-align:center;">No orders found.</p>
    <%
        } else {
    %>
        <table class="user-details">
            <thead>
                <tr>
                    <th>Order Date</th>
                    <th>Status</th>
                    <th>Total Amount</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Iterator it = orders.iterator(); it.hasNext();) {
                        Order order = (Order) it.next();
                %>
                <tr>
                    <td><%= order.getOrderDate() %></td>
                    <td><%= order.getStatus() %></td>
                    <td><%= order.getTotalAmount() %></td>
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
