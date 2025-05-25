<%@ page import="java.util.List" %>
<%@ page import="iotbay.model.User" %>
<%@ page import="iotbay.dao.UserDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="iotbay.dao.DBManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>View Users - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <nav class="page-nav">
    <div class="nav-left">
      <a href="index.jsp">Home</a>
      <a href="main.jsp">Main</a>
    </div>
    <div class="nav-right">
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>

  <div class="dashboard-container">
    <h1 class="dashboard-title">User Management</h1>

    <div class="dashboard-section">
      <a href="createUser.jsp" class="btn-add-shipment">Create New User</a>
    </div>

    <div class="dashboard-section">
      <h2>All Users</h2>
      <table class="user-details wide">
        <tr>
          <th>Email</th>
          <th>Name</th>
          <th>Phone</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>

        <%
          DBManager manager = (DBManager) session.getAttribute("manager");
          User currentUser = (User) session.getAttribute("currentUser");
          boolean isAdmin = currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
          
          if (manager != null) {
              UserDAO userDAO = new UserDAO(manager.getConnection());
              List<User> users = userDAO.getAllUsers();
              for (User u : users) {
                  boolean isAdminAccount = "admin@example.com".equalsIgnoreCase(u.getEmail());
                  boolean canModify = isAdmin || !isAdminAccount;
        %>
        <tr>
          <td><%= u.getEmail() %></td>
          <td><%= u.getFirstName() + " " + u.getLastName() %></td>
          <td><%= u.getPhone() %></td>
          <td><%= u.getRole() %></td>
          <td>
            <% if (u.isActive()) { %>
              <span class="status-badge status-processing">Active</span>
            <% } else { %>
              <span class="status-badge status-delivered">Inactive</span>
            <% } %>
          </td>
          <td>
            <% if (canModify) { %>
              <a href="UserServlet?action=edit&email=<%= u.getEmail() %>" 
                 class="btn-secondary" style="padding: 6px 10px; font-size: 13px;">Edit</a>
            
              <% if (!isAdminAccount) { %>
                <a href="UserServlet?action=delete&email=<%= u.getEmail() %>" 
                   class="btn-important" style="padding: 6px 10px; font-size: 13px;"
                   onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>

                <% String action = u.isActive() ? "deactivate" : "activate"; %>
                <a href="UserServlet?action=toggleStatus&email=<%= u.getEmail() %>" 
                   class="btn-secondary" style="padding: 6px 10px; font-size: 13px;"
                   onclick="return confirm('Are you sure you want to <%= action %> this user?');">
                   <%= u.isActive() ? "Deactivate" : "Activate" %>
                </a>
              <% } %>
            <% } else { %>
              <span class="text-muted">No actions available</span>
            <% } %>
          </td>
        </tr>
        <%
              }
          } else {
        %>
        <tr>
          <td colspan="6">Error: DBManager not initialized. Please log in again.</td>
        </tr>
        <%
          }
        %>
      </table>
    </div>
  </div>
</body>
</html>
