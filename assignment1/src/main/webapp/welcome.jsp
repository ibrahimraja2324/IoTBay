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
       <a href="main.jsp">Home</a>
    </div>
  </nav>

  <div class="index-content">
    <%
      User currentUser = (User) session.getAttribute("currentUser");
      if (currentUser == null) {
        response.sendRedirect("login.jsp");
      }
    %>
    <%
      String welcomeMessage = (String) session.getAttribute("welcomeMessage");
      if (welcomeMessage == null) {
        welcomeMessage = "Welcome!";
      }
      session.removeAttribute("welcomeMessage"); // Clear it so it doesn't show next time
    %>
    <h2><%= welcomeMessage %></h2>
    <p>These are your details:</p>

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
        <td>
          <%
            String password = currentUser.getPassword();
            if (password != null) {
              for (int i = 0; i < password.length(); i++) out.print("*");
            }
          %>
        </td>
      </tr>
    </table>
    
    <div class="action-buttons">
      <a href="main.jsp" class="btn-primary">Go to Main Page</a>
      <a href="LogoutServlet" class="btn-secondary">Logout</a>
    </div>

  </body>
</html>
