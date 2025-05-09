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
    <div class="nav-right">
       <a href="login.jsp">Login</a>
       <a href="register.jsp">Register</a>
    </div>
  </nav>
  
  <div id="welcome-page">
    <div class="welcome-box">
      <%
        User newUser = (User) session.getAttribute("currentUser");
        if (newUser == null) {
            response.sendRedirect("login.jsp");
        }
      %>

  <h2>Registration Successful!</h2>
  <p>The following details have been registered:</p>
  <ul>
      <li>Full Name: <%= newUser.getFirstName() + " " + newUser.getLastName() %></li>
      <li>Email: <%= newUser.getEmail() %></li>
      <li>Phone: <%= newUser.getPhone() %></li>
  </ul>
        
        <h2>Registration Successful!</h2>
        <p>The following details have been registered:</p>
        <ul>
          <li>Full Name: <%= newUser.getFirstName() + " " + newUser.getLastName() %></li>
          <li>Email: <%= newUser.getEmail() %></li>
          <li>Phone: <%= newUser.getPhone() %></li>
        </ul>
        
        <p>Please choose an option:</p>
        <a href="login.jsp">Return to Login</a>
        <a href="main.jsp">Proceed to Main</a>
      </div>
    </div>
    
  </body>
</html>
