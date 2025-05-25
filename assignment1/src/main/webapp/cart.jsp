<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Cart, iotbay.model.CartItem, iotbay.model.Device, iotbay.model.User" %>

<%  
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null) {
        cart = new Cart(new ArrayList<>());
        session.setAttribute("cart", cart);
    }

    List<CartItem> items = cart.getItems();
    String errorMessage = (String) session.getAttribute("checkoutError");
    if (errorMessage != null) {
        session.removeAttribute("checkoutError");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Your Cart – IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <style>
    .details-table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 16px;
    }
    .details-table th, .details-table td {
      padding: 10px;
      border: 1px solid #ccc;
      text-align: center;
    }
    .btn {
      padding: 8px 16px;
      background-color: #007BFF;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      height: 38px;
    }
    .btn:hover {
      background-color: #0056b3;
    }
    .btn-danger {
      background-color: #dc3545;
    }
    .btn-danger:hover {
      background-color: #b52a37;
    }
    .btn-large {
      min-width: 200px;
      padding: 12px 24px;
      height: 48px;
      font-size: 16px;
      display: inline-block;
      margin: 0 6px;
      text-decoration: none;
      line-height: 1;
    }
    .bottom-nav {
      position: fixed;
      bottom: 0;
      width: 100%;
      background: #f9f9f9;
      padding: 20px 0;
      text-align: center;
      box-shadow: 0 -2px 6px rgba(0,0,0,0.1);
    }
    .main-box {
      padding: 40px;
      margin-bottom: 160px;
    }
    input[type="number"] {
      width: 60px;
      padding: 6px;
      height: 38px;
      text-align: center;
    }
    form.inline {
      display: inline;
    }
    .alert {
      background: #ffe0e0;
      color: #a00;
      padding: 12px 20px;
      margin-bottom: 20px;
      border-radius: 6px;
      text-align: center;
    }
  </style>
</head>
<body>

<div class="main-box">
  <h2>Your Cart</h2>

  <% if (errorMessage != null) { %>
    <div class="alert"><%= errorMessage %></div>
  <% } %>

  <% if (items == null || items.isEmpty()) { %>
    <p>Your cart is currently empty.</p>
  <% } else { %>
    <table class="details-table">
      <thead>
        <tr>
          <th>Device</th>
          <th>Price</th>
          <th>Qty</th>
          <th>Total</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <%
          double totalAmount = 0;
          for (CartItem item : items) {
              Device device = item.getDevice();
              int quantity = item.getQuantity();
              double total = device.getUnitPrice() * quantity;
              totalAmount += total;
        %>
        <tr>
          <td><%= device.getName() %></td>
          <td>$<%= String.format("%.2f", device.getUnitPrice()) %></td>
          <td>
            <form action="updateQuantity" method="post" class="inline">
              <input type="hidden" name="deviceId" value="<%= device.getId() %>">
              <input type="number" name="quantity" value="<%= quantity %>" min="1">
              <button type="submit" class="btn">Update</button>
            </form>
          </td>
          <td>$<%= String.format("%.2f", total) %></td>
          <td>
            <form action="removeFromCart" method="post" class="inline">
              <input type="hidden" name="deviceId" value="<%= device.getId() %>">
              <button type="submit" class="btn btn-danger">Remove</button>
            </form>
          </td>
        </tr>
        <% } %>
      </tbody>
    </table>
    <p><strong>Total:</strong> $<%= String.format("%.2f", totalAmount) %></p>
  <% } %>
</div>

<div class="bottom-nav">
  <a href="products.jsp" class="btn btn-large">← Back to Products</a>
  <form action="checkout.jsp" method="get" style="display:inline;">
    <button class="btn btn-large" type="submit">Proceed to Checkout →</button>
  </form>
</div>

</body>
</html>