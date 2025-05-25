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
      <a href="index.jsp">Home</a>
      <a href="main.jsp">Main</a>
    </div>
    <div class="nav-right">
      <a href="viewuser.jsp">Back to User List</a>
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>

  <div class="dashboard-container">
    <h1 class="dashboard-title">Create New User</h1>

    <div class="payment-form-container">
      <form action="UserServlet?action=create" method="post">
        <h2>User Details</h2>

        <label for="firstName">First Name:</label>
        <input type="text" name="firstName" required>

        <label for="lastName">Last Name:</label>
        <input type="text" name="lastName" required>

        <label for="email">Email:</label>
        <input type="text" name="email" required>

        <label for="password">Password:</label>
        <input type="password" name="password" required>

        <label for="phone">Phone Number:</label>
        <input type="text" name="phone" required>

        <label for="role">Role:</label>
        <select name="role" required>
          <option value="">-- Select Role --</option>
          <option value="USER">User</option>
          <option value="STAFF">Staff</option>
        </select>

        <label for="active">Status:</label>
        <select name="active" required>
          <option value="1">Active</option>
          <option value="0">Inactive</option>
        </select>

        <input type="submit" value="Create User">
      </form>
    </div>
  </div>
</body>
</html>
