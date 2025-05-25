<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" id="login-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - IoTBay</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
  
    <nav class="page-nav">
       <div class="nav-left">
          <a href="main.jsp">Home</a>
       </div>
    </nav>
    
    <form class="login-box" action="LoginServlet" method="post">
        <h2>Welcome Back</h2>
        
        <% 
            String emailError = (String) session.getAttribute("emailError");
            String passwordError = (String) session.getAttribute("passwordError");
            String loginError = (String) session.getAttribute("loginError");
            if (emailError != null) { 
        %>
            <p style="color: red;"><%= emailError %></p>
        <% 
            } 
            if (passwordError != null) { 
        %>
            <p style="color: red;"><%= passwordError %></p>
        <% 
            } 
            if (loginError != null) { 
        %>
            <p style="color: red;"><%= loginError %></p>
        <% 
            } 
        %>
        
        <input type="text" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="submit" value="Login">
        <a href="register.jsp">Don't have an account? Register</a>
    </form>
</body>
</html>
