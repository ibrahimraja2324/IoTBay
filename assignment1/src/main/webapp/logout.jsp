<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Logout - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <%
    session.removeAttribute("currentUser");
  %>
  <nav class="page-nav">
        <div class="nav-left">
            <a href="index.jsp">Home</a>
        </div>
    </nav>

  <div class="index-content">
    <h2>You have logged out successfully!</h2>
    <div class="action-buttons">
      <a href="index.jsp" class="btn-primary">Go to Home</a>
      <a href="login.jsp" class="btn-secondary">Login Again</a>
    </div>
  </div>
</body>
</html>
