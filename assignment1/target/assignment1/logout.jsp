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
    session.invalidate();
  %>
  <div id="logout-page">
    <div class="logout-box">
      <h2>You have logged out successfully!</h2>
      <p>
        <a href="index.jsp" class="btn-home">Back to Home Page</a>
      </p>
    </div>
  </div>
</body>
</html>
