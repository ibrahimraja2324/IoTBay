<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Create User - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <nav class="page-nav">
    <div class="nav-left">
      <a href="main.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="viewuser.jsp">Back to User List</a>
      <a href="LogoutServlet">Logout</a>
    </div>
  </nav>

  <div class="dashboard-container">
    <h1 class="dashboard-title">Create New User</h1>

    <div class="payment-form-container">
      <form action="UserServlet?action=create" method="post">
        <h2>User Details</h2>

        <label for="firstName">First Name:</label>
        <input type="text" name="firstName" pattern="[A-Za-z ]+" title="Please enter letters only">

        <label for="lastName">Last Name:</label>
        <input type="text" name="lastName" pattern="[A-Za-z ]+" title="Please enter letters only">

        <label for="email">Email:</label>
        <input type="text" name="email">

        <label for="password">Password:</label>
        <input type="password" name="password">

        <label for="phone">Phone Number:</label>
        <input type="tel" name="phone" pattern="[0-9]+" title="Please enter numbers only">

        <label for="role">Role:</label>
        <select name="role">
          <option value="">-- Select Role --</option>
          <option value="USER">User</option>
          <option value="STAFF">Staff</option>
        </select>

        <label for="active">Status:</label>
        <select name="active">
          <option value="1">Active</option>
          <option value="0">Inactive</option>
        </select>

        <input type="submit" value="Create User">
      </form>
    </div>
  </div>
</body>
</html>
