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
  <h1 style="text-align:center;">Your Payment Methods</h1>
  
  <!-- Updated Search Form using .search-container and .btn-submit -->
  <div class="search-container" style="text-align:center; margin-bottom:20px;">
    <form action="PaymentMethodServlet" method="get">
      <input type="hidden" name="action" value="list">
      <input type="text" name="search" placeholder="Search by name or last 4 digits" 
             style="width:300px; padding:8px; font-size:16px;">
      <button type="submit" class="btn-submit">Search</button>
    </form>
  </div>
  
  <%
     List<Payment> paymentMethods = (List<Payment>) request.getAttribute("paymentMethods");
     if (paymentMethods != null && !paymentMethods.isEmpty()) {
  %>
  <table class="user-details">
    <thead>
      <tr>
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
         for (Payment pm : paymentMethods) {
      %>
      <tr>
         <td><%= pm.getPaymentMethod() %></td>
         <td><%= pm.getCardHolderName() %></td>
         <td><%= pm.getCardNumber().replaceAll(".(?=.{4})", "*") %></td>
         <td><%= pm.getCvv() %></td>
         <td><%= pm.getExpiryDate() %></td>
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
     } else {
  %>
  <p style="text-align:center;">No payment methods found.</p>
  <%
     }
  %>
</body>
</html>
