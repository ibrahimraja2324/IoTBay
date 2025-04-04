<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - IoTBay</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <form class="login-box" action="RegisterServlet" method="post">
        <h2>Create Account</h2>
        <input type="text" name="name" placeholder="Full Name" required>
        <input type="text" name="phone" placeholder="Phone Number" required>
        <input type="text" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="password" placeholder="Confirm Password" required>
        <input type="submit" value="Register">
        <a href="login.jsp">Already have an account? Login</a>
    </form>
</body>
</html>
