<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Payment" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Payment Methods</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <h1>Your Payment Methods</h1>
  <table>
    <thead>
      <tr>
         <th>Payment ID</th>
         <th>Payment Method</th>
         <th>Card Holder Name</th>
         <th>Card Number</th>
         <th>CVV</th>
         <th>Expiry Date</th>
         <th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <%
         List<Payment> paymentMethods = (List<Payment>) request.getAttribute("paymentMethods");
         if (paymentMethods != null && !paymentMethods.isEmpty()) {
             for (Payment pm : paymentMethods) {
      %>
      <tr>
         <td><%= pm.getPaymentId() %></td>
         <td><%= pm.getPaymentMethod() %></td>
         <td><%= pm.getCardHolderName() %></td>
         <td><%= pm.getCardNumber() %></td>
         <td><%= pm.getCvv() %></td>
         <td><%= pm.getExpiryDate() %></td>
         <td>
           <a href="PaymentMethodServlet?action=edit&paymentId=<%= pm.getPaymentId() %>">Edit</a> |
           <a href="PaymentMethodServlet?action=delete&paymentId=<%= pm.getPaymentId() %>" onclick="return confirm('Delete this payment method?');">Delete</a>
         </td>
      </tr>
      <%
             }
         } else {
      %>
      <tr>
         <td colspan="7">No payment methods found.</td>
      </tr>
      <%
         }
      %>
    </tbody>
  </table>
</body>
</html>
