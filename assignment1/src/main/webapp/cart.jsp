<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, iotbay.model.Cart, iotbay.model.CartItem, iotbay.model.Device" %>

<%
    Cart cart = (Cart) session.getAttribute("cart");
    if (cart == null) {
        cart = new Cart();

        // Sample devices
        Device d1 = new Device(1, "Smart Sensor", "Sensor", 59.99, 100);
        Device d2 = new Device(2, "IoT Camera", "Camera", 129.99, 50);
        Device d3 = new Device(3, "Gateway Hub", "Hub", 199.99, 30);

        cart.addItem(new CartItem(d1, 1));
        cart.addItem(new CartItem(d2, 1));
        cart.addItem(new CartItem(d3, 1));

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
    }
    .btn {
      padding: 8px 16px;
      background-color: #007BFF;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn:hover {
      background-color: #0056b3;
    }
    .bottom-nav {
      position: fixed;
      bottom: 0;
      width: 100%;
      background: #f9f9f9;
      padding: 12px 0;
      text-align: center;
      box-shadow: 0 -2px 6px rgba(0,0,0,0.1);
    }
    .main-box {
      padding: 40px;
      margin-bottom: 140px;
    }
    input[type="number"] {
      width: 60px;
      padding: 4px;
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
              <button type="submit" class="btn" style="background:#dc3545;">Remove</button>
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
  <a href="products.jsp" class="btn">← Back to Products</a>
  <form action="checkout.jsp" method="get" style="display:inline;">
    <button class="btn" type="submit">Proceed to Checkout →</button>
  </form>
</div>

</body>
</html>