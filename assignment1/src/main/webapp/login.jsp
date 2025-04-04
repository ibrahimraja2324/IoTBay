<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - IoTBay</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <form class="login-box" action="LoginServlet" method="post">
        <h2>Welcome Back</h2>
        <c:if test="${not empty errorMessage}">
            <p style="color: red;">${errorMessage}</p>
        </c:if>
        <input type="text" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="submit" value="Login">
        <a href="register.jsp">Don't have an account? Register</a>
    </form>
</body>
</html>