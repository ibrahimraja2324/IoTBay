<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" id="index-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IoTBay Home</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
 
    <nav class="page-nav">
       <div class="nav-left">
          <a href="index.jsp">Home</a>
          <a href="LogServlet">Logs (ONLY FOR TESTING)</a>
       </div>
    </nav>
    
   
    <div class="index-content">
        <h2>Welcome to IoTBay!</h2>
        <p>Your one-stop solution for IoT Devices</p>

        <div class="action-buttons">
            <a href="login.jsp" class="btn-primary">Login</a>
            <a href="register.jsp" class="btn-secondary">Register</a>
            <a href="GuestLoginServlet" class="btn-secondary">Continue as Guest</a>
        </div>
    </div>
    <jsp:include page="/ConnServlet" flush="true" />
</body>
</html>
