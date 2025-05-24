<%@ page import="iotbay.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit User - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <nav class="page-nav">
    <div class="nav-left">
      <a href="index.jsp">Home</a>
      <a href="main.jsp">Main</a>
    </div>
    <div class="nav-right">
      <a href="viewuser.jsp">Back to User List</a>
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>

  <div class="dashboard-container">
    <h1 class="dashboard-title">Edit User</h1>

    <div class="payment-form-container">
      <%
        User user = (User) request.getAttribute("user");
        if (user == null) {
      %>
        <p style="color:red;">User not found or an error occurred.</p>
      <%
        } else {
      %>
      <form action="UserServlet?action=update" method="post">
        <h2>User Details</h2>

        <label for="firstName">First Name:</label>
        <input type="text" name="firstName" value="<%= user.getFirstName() %>" required>

        <label for="lastName">Last Name:</label>
        <input type="text" name="lastName" value="<%= user.getLastName() %>" required>

        <label for="email">Email:</label>
        <input type="text" name="email" value="<%= user.getEmail() %>" readonly>

        <label for="password">Password:</label>
        <input type="text" name="password" value="<%= user.getPassword() %>" required>

        <label for="phone">Phone Number:</label>
        <input type="text" name="phone" value="<%= user.getPhone() %>" required>

        <input type="submit" value="Update User">
      </form>
      <% } %>
    </div>
  </div>
</body>
</html>
