<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Payment" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Payment Methods</title>
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
    <h1>Your Payment Methods</h1>
    <%
        List paymentMethods = (List) request.getAttribute("paymentMethods");
        if (paymentMethods == null || paymentMethods.isEmpty()) {
    %>
        <p>No payment methods found.</p>
    <%
        } else {
    %>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Payment Method</th>
                    <th>Card Details</th>
                    <th>Expiry Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Iterator it = paymentMethods.iterator(); it.hasNext();) {
                        Payment pm = (Payment) it.next();
                %>
                <tr>
                    <td><%= pm.getPaymentId() %></td>
                    <td><%= pm.getPaymentMethod() %></td>
                    <td><%= pm.getCardDetails() %></td>
                    <td><%= pm.getDate() %></td>
                    <td>
                        <a href="PaymentMethodServlet?action=edit&paymentId=<%= pm.getPaymentId() %>">Edit</a> |
                        <a href="PaymentMethodServlet?action=delete&paymentId=<%= pm.getPaymentId() %>" onclick="return confirm('Delete this payment method?');">Delete</a>
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
</body>
</html>
