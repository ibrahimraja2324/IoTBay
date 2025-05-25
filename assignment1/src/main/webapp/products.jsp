<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="iotbay.model.Device" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Products</title>
    <style>
        body {
  background-color: #1f1c2c;
  color: #ffffff;
  font-family: 'Quicksand', sans-serif;
  padding-top: 80px;
}
.page-nav {
  background: #22203a;
  color: #a6a6ff;
  padding: 10px 20px;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 100;
}

.page-nav .nav-left a,
.page-nav .nav-right a {
  color: #a6a6ff;
  margin-right: 15px;
  text-decoration: none;
  font-weight: 600;
}

.page-nav .nav-right {
  float: right;
}

.page-nav a:hover {
    background-color: #555;
}

h1 {
  text-align: center;
  font-size: 2em;
  margin-bottom: 20px;
  color: #a6a6ff;
}

.search-container {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.search-container input[type="text"],
.search-container select {
  background-color: #3a3760;
  border: none;
  color: #fff;
  padding: 10px;
  font-size: 16px;
  border-radius: 6px;
  width: 200px;
}

.search-container button {
  background: linear-gradient(to right, #2575fc, #6a11cb);
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
}

.search-container button:hover {
  background: linear-gradient(to right, #6a11cb, #2575fc);
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.card {
  background: #28243c;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(30, 22, 54, 0.25);
  text-align: center;
  color: #e0e0e0;
}

.card h3 {
  margin-top: 0;
  color: #ffffff;
}

.card p {
  margin: 8px 0;
}

.card small {
  color: #a6a6ff;
}

.card form,
.card button {
  margin-top: 10px;
}

.card button,
.card input[type="submit"] {
  padding: 10px 16px;
  font-size: 14px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
}

.submit-button,
.remove-button {
  padding: 12px 20px;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-button {
  background: linear-gradient(to right, #2575fc, #6a11cb);
  color: #000;
  text-decoration: none;
  text-align: center;
}

.submit-button:hover {
  background: linear-gradient(to right, #6a11cb, #2575fc);
}

.remove-button {
  background-color: #e01c0a;
  color: #000;
  text-decoration: none;
  text-align: center;
}

.remove-button:hover {
  background-color: #d4ac0d;
  color: #000;
}
    </style>
    <script>
        function buyDevice(name) {
          alert("✅ You have successfully purchased " + name + "!");
        }
      </script>
</head>
<body>
<% User currentUser = (User) session.getAttribute("currentUser"); %>
<nav class="page-nav">
    <div class="nav-left">
      <a href="main.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="logout.jsp">Logout</a>
      <% if (currentUser != null && !"GUEST".equalsIgnoreCase(currentUser.getRole())) { %>
        <a href="payment-dashboard.jsp">Manage Payments</a>
        <a href="shipment-dashboard.jsp">Manage Shipments</a>
      <% } %>
      <% if (currentUser != null && "STAFF".equalsIgnoreCase(currentUser.getRole())) { %>
        <a href="viewuser.jsp">View Users</a>
      <% } %>
      <% if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) { %>
        <a href="viewuser.jsp">View Users</a>
      <% } %>
    </div>
  </nav>
<h1>Available Devices</h1>

<div class="search-container">
    <form action="devices" method="get">
      <input type="hidden" name="action" value="search">
      <input type="text" name="keyword" placeholder="Search by name">
      
      <select name="type">
          <option value="">All Types</option>
          <option value="Phone">Phone</option>
          <option value="Wearable">Wearable</option>
          <option value="Home">Home</option>
          <option value="Security">Security</option>
      </select>

      <button type="submit">Search / Filter</button>
  </form>
</div>

<% if (currentUser != null && "STAFF".equalsIgnoreCase(currentUser.getRole())) { %>
<div style="text-align:center;">
    <form action="devices" method="get">
        <input type="hidden" name="action" value="addNewDevice">
        <button type="submit">+ Add New Device</button>
    </form>
</div>
<% } %>

<div class="grid">
    <% List<Device> list = (List<Device>) request.getAttribute("devices");
       if (list != null) {
           for (Device d : list) {
    %>
    <div class="card">
        <h3><%= d.getName() %></h3>
        <p><strong>Type:</strong> <%= d.getType() %></p>
        <p><strong>Price:</strong> $<%= d.getUnitPrice() %></p>
        <p><strong>Quantity:</strong> <%= d.getQuantity() %></p>
        <p><small>ID: <%= d.getId() %></small></p>
        <% if (currentUser != null && "STAFF".equalsIgnoreCase(currentUser.getRole())) { %>
            <form action="devices" method="post" style="display:inline-block;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= d.getId() %>">
                <button type="submit" class="remove-button">Remove</button>
            </form>
            <form action="devices" method="get" style="display:inline-block; margin-left: 5px;">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="id" value="<%= d.getId() %>">
                <button type="submit" class="edit-button">Edit</button>
            </form>
        <% } else { %>
            <button class="buy-button" onclick="buyDevice('<%= d.getName() %>')">Buy Now</button>
        <% } %>
    </div>
    <% } } %>
</div>
</body>
</html>
