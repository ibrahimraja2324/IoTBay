<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Welcome - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>

  <nav class="page-nav">
    <div class="nav-left">
       <a href="index.jsp">Home</a>
    </div>
  </nav>

  <div class="index-content">
    <%
      User currentUser = (User) session.getAttribute("currentUser");
      if (currentUser == null) {
        response.sendRedirect("login.jsp");
      }
    %>
    <h2>Registration Successful!</h2>
    <p>The following details have been registered:</p>

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
      <tr>
        <td>Password</td>
        <td><%= currentUser.getPassword() %></td>
      </tr>
    </table>
    
    <div class="action-buttons">
      <a href="main.jsp" class="btn-primary">Go to Main Page</a>
      <a href="logout.jsp" class="btn-secondary">Logout</a>
    </div>

  </body>
</html>
