<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Main - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>

  <%
    User currentUser = (User) session.getAttribute("currentUser");
  %>

  <nav class="page-nav">
    <div class="nav-left">
      <a href="index.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="logout.jsp">Logout</a>
      <a href="payment-dashboard.jsp">Manage Payments</a>
      <a href="shipment-dashboard.jsp">Manage Shipments</a>
        <% if (currentUser != null && "STAFF".equalsIgnoreCase(currentUser.getRole())) { %>
          <a href="viewuser.jsp">View Users</a>
        <% } %>
        <% if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) { %>
          <a href="viewuser.jsp">View Users</a>
        <% } %>
      <a href="http://localhost:8080/assignment1/devices">Products</a>
    </div>
  </nav>

  <div class="index-content">
    <%
      if (currentUser != null) {
    %>
      <h2>Welcome, <%= (currentUser.getFirstName() + " " + currentUser.getLastName()).trim().isEmpty() ? currentUser.getEmail() : (currentUser.getFirstName() + " " + currentUser.getLastName()) %>!</h2>
      <p>Your details are as follows:</p>
      <table class="user-details">
        <tr>
          <th>Field</th>
          <th>Value</th>
        </tr>
        <tr>
          <td>Full Name</td>
          <td><%= currentUser.getFirstName() + " " + currentUser.getLastName() %></td>
        </tr>
        <tr>
          <td>Email</td>
          <td><%= currentUser.getEmail() %></td>
        </tr>
        <tr>
          <td>Phone</td>
          <td><%= currentUser.getPhone() %></td>
        </tr>
      </table>

      <div class="action-buttons">
        <a href="edit.jsp" class="btn-primary">Edit Details</a>
        <a href="LogServlet" class="btn-secondary">View Your Logs</a>
      </div>
    <%
      } else {
    %>
      <p>No user information is available. Please <a href="login.jsp">log in</a> again.</p>
    <%
      }
    %>
  </div>

</body>
</html>
