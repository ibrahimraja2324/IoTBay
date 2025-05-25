<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Order" %>
<%@ page import="iotbay.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    iotbay.model.User currentUser = (iotbay.model.User) session.getAttribute("currentUser");
    if (currentUser == null) {
        response.sendRedirect("login.jsp"); 
        return;
    }

    List<Order> orderList = (List<Order>) request.getAttribute("orderList");
    String currentSortBy = (String) request.getAttribute("currentSortBy");
    String currentSortOrder = (String) request.getAttribute("currentSortOrder");

    String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
        session.removeAttribute("successMessage");
    }

    if (currentSortBy == null) currentSortBy = "orderId";
    if (currentSortOrder == null) currentSortOrder = "asc";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Order List – IoTBay</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <style>
    .sort-buttons { margin:20px 0; display:flex; flex-wrap:wrap; gap:12px; justify-content:center }
    .btn {
      background-color: #007BFF;
      color: white;
      padding: 8px 16px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn:hover { background-color: #0056b3; }
    table {
      width: 100%; border-collapse: collapse; margin-top: 20px;
    }
    th, td {
      padding: 10px; border: 1px solid #ccc; text-align: left;
    }
    .main-box {
      max-width: 960px;
      margin: auto;
      background: white;
      padding: 32px;
      border-radius: 10px;
      box-shadow: 0 2px 10px #00000020;
    }
    .link {
      color: #007BFF;
      text-decoration: none;
    }
    .link:hover { text-decoration: underline; }
    form.inline-form { display: inline; margin: 0; padding: 0; }
    input.qty-input {
      width: 50px;
      padding: 4px;
      margin-right: 8px;
    }
    .alert-success {
      background: #e0f9e0;
      color: #1a7e1a;
      padding: 12px 20px;
      margin-bottom: 20px;
      border-radius: 6px;
      text-align: center;
    }
  </style>
</head>
<body class="gradient-bg">

<nav class="page-nav">
  <div class="nav-left">
    <a href="index.jsp">Home</a>
  </div>
  <div class="nav-right">
    <a href="logout.jsp">Logout</a>
  </div>
</nav>

<div class="main-box">
  <h2>Order List</h2>

  <% if (successMessage != null && !successMessage.trim().isEmpty()) { %>
    <div class="alert-success"><%= successMessage %></div>
  <% } %>

  <div class="sort-buttons">
    <form action="listOrders" method="get">
      <input type="hidden" name="sortBy" value="orderId">
      <input type="hidden" name="sortOrder" value="<%= ("orderId".equals(currentSortBy) && "asc".equals(currentSortOrder)) ? "desc" : "asc" %>">
      <button class="btn" type="submit">
        Sort by Order ID (<%= "orderId".equals(currentSortBy) ? currentSortOrder : "asc" %>)
      </button>
    </form>

    <form action="listOrders" method="get">
      <input type="hidden" name="sortBy" value="orderDate">
      <input type="hidden" name="sortOrder" value="<%= ("orderDate".equals(currentSortBy) && "asc".equals(currentSortOrder)) ? "desc" : "asc" %>">
      <button class="btn" type="submit">
        Sort by Date (<%= "orderDate".equals(currentSortBy) ? currentSortOrder : "asc" %>)
      </button>
    </form>

    <form action="listOrders" method="get" style="display:flex;gap:6px;align-items:center">
      <select name="searchType" class="btn" style="background:#fff;color:#333">
        <option value="orderId">Order ID</option>
        <option value="orderDate">Order Date</option>
      </select>
      <input type="text" name="searchTerm" placeholder="Enter search term…" style="padding:6px 10px;border:1px solid #ccc;border-radius:4px">
      <button class="btn" type="submit">Search</button>
    </form>
  </div>

  <table>
    <thead>
      <tr>
        <th>ID</th><th>Date</th><th>Status</th><th>Delivery</th><th>Qty</th><th>Actions</th>
      </tr>
    </thead>
    <tbody>
      <%
        if (orderList != null && !orderList.isEmpty()) {
          for (Order order : orderList) {
      %>
        <tr>
          <td><a class="link" href="orderDetails?orderId=<%= order.getOrderId() %>"><%= order.getOrderId() %></a></td>
          <td><%= order.getOrderDate() != null ? formatter.format(order.getOrderDate()) : "N/A" %></td>
          <td><%= order.getStatus() %></td>
          <td><%= order.getDeliveryAddress() != null ? order.getDeliveryAddress() : "N/A" %></td>
          <td>
            <form action="updateOrderQuantity" method="post" class="inline-form">
              <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
              <input type="number" name="quantity" min="1" class="qty-input" value="<%= order.getQuantity() != null ? order.getQuantity() : "1" %>">
              <button type="submit" class="btn">Update</button>
            </form>
          </td>
          <td>
            <form action="deleteOrder" method="post" class="inline-form" onsubmit="return confirm('Are you sure you want to delete this order?');">
              <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
              <button type="submit" class="btn" style="background:#dc3545">Delete</button>
            </form>
          </td>
        </tr>
      <%
          }
        } else {
      %>
        <tr><td colspan="6">No orders found.</td></tr>
      <%
        }
      %>
    </tbody>
  </table>

  <div style="text-align:right;margin-top:24px">
    <a class="btn" href="index.jsp">Return to Home</a>
  </div>
</div>

</body>
</html>