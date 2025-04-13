<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" id="register-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - IoTBay</title>
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
    
 <!-- might have to change the action from welcome.jsp to a method that posts to SQL for assignment 2. -->
    <form class="login-box" action="welcome.jsp" method="post">
        <h2>Create Account</h2>
        <input type="text" name="name" placeholder="Full Name" required>
        <input type="text" name="phone" placeholder="Phone Number" required>
        <input type="text" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
        <input type="submit" value="Register">
        <a href="login.jsp">Already have an account? Login</a>
    </form>
</body>
</html>
