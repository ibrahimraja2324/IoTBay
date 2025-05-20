<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Order" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order History</title>
    <style>
        table { 
            border-collapse: collapse; 
            width: 100%; 
        }
        table, th, td { 
            border: 1px solid #ccc; 
        }
        th, td { 
            padding: 8px; 
            text-align: center; 
        }
        th { 
            background-color: #f0f0f0; 
        }
    </style>
</head>
<body>
    <h1>Order History</h1>
    <%
        List orders = (List) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
    %>
        <p>No orders found.</p>
    <%
        } else {
    %>
        <table>
            <thead>
                <tr>
                    <th>Order ID</th>
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
                    <td><%= order.getOrderId() %></td>
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
