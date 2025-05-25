<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<%
    User user = (User) session.getAttribute("currentUser");
    String editError = (String) session.getAttribute("editError");
    String passwordError = (String) session.getAttribute("passwordError");
    String dbError = (String) session.getAttribute("dbError");

    // Clear error messages from session after displaying
    session.removeAttribute("editError");
    session.removeAttribute("passwordError");
    session.removeAttribute("dbError");
%>
<!DOCTYPE html>
<html lang="en" id="edit-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile - IoTBay</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
            <a href="main.jsp">Home</a>
        </div>
    </nav>

    <form class="login-box" action="EditProfileServlet" method="post">
        <h2>Edit Your Profile</h2>

        <% 
          if (editError != null || passwordError != null || dbError != null) { 
        %>
          <div class="error-messages">
            <% if (editError != null) { %>
              <p><%= editError %></p>
            <% } %>
            <% if (passwordError != null) { %>
              <p><%= passwordError %></p>
            <% } %>
            <% if (dbError != null) { %>
              <p><%= dbError %></p>
            <% } %>
          </div>
        <% } %>

        <label class="heading" for="firstName">First Name</label>
        <input type="text" name="firstName" id="firstName" placeholder="First Name"
               value="<%= user != null && user.getFirstName() != null ? user.getFirstName() : "" %>" required>
        <label class="heading" for="lastName">Last Name</label>
        <input type="text" name="lastName" id="lastName" placeholder="Last Name"
               value="<%= user != null && user.getLastName() != null ? user.getLastName() : "" %>" required>
        <label class="heading" for="email">Email</label>
        <input type="text" name="email" id="email" placeholder="Email"
               value="<%= user != null && user.getEmail() != null ? user.getEmail() : "" %>" disabled>
        <label class="heading" for="currentPassword">Current Password</label>
        <input type="password" name="currentPassword" id="currentPassword" placeholder="Current Password" required>
        <label class="heading" for="password">New Password</label>
        <input type="password" name="password" id="password" placeholder="New Password">
        <label class="heading" for="confirmPassword">Confirm New Password</label>
        <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm New Password">
        <label class="heading" for="phone">Phone Number</label>
        <input type="text" name="phone" id="phone" placeholder="Phone Number"
               value="<%= user != null && user.getPhone() != null ? user.getPhone() : "" %>" required>
        <input type="submit" value="Save Changes">
        <a href="delete-confirmation.jsp" class="btn-important">Delete account</a>
        <a href="main.jsp">Cancel</a>
    </form>
</body>
</html>