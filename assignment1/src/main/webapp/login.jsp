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
          <a href="index.jsp">Home</a>
       </div>
    </nav>
    
 
    <form class="login-box" action="${pageContext.request.contextPath}/LoginServlet" method="post">
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
