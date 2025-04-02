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
  
  <!-- Wrap the welcome content in a container matching the other pages' naming conventions -->
  <div id="welcome-page">
    <div class="welcome-box">
      <%
        // Retrieve all parameters from the register.jsp form
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String emailParam = request.getParameter("email");
        String passwordParam = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // (Optional: You might wish to check if password matches confirmPassword)
        
        // Create a new User object using these parameters
        User newUser = new User();
        newUser.setName(name);
        newUser.setPhone(phone);
        newUser.setEmailAddress(emailParam);
        newUser.setPassword(passwordParam);
        
        // Save the User object to session so it can be retrieved later in main.jsp
        session.setAttribute("currentUser", newUser);
      %>
      
      <h2>Registration Successful!</h2>
      <p>The following details have been registered:</p>
      <ul>
        <li>Full Name: <%= newUser.getName() %></li>
        <li>Email: <%= newUser.getEmailAddress() %></li>
        <li>Phone: <%= newUser.getPhone() %></li>
      </ul>
      
      <p>Please choose an option:</p>
      <a href="login.jsp">Return to Login</a>
      <a href="main.jsp">Proceed to Main</a>
    </div>
  </div>
  
</body>
</html>
