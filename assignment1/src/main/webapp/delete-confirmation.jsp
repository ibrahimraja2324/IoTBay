<%@ page import="java.util.List" %>
<%@ page import="iotbay.model.Log" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Logs</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
            <a href="index.jsp">Home</a>
        </div>
    </nav>
    <div class="index-content">
        <h2>Are you sure you want to delete your account?</h2>
        <p>This action cannot be undone.</p>
        <form action="DeleteAccountServlet" class="login-box" style="margin: 0px auto;" method="post">
            <div class="action-buttons">
                <input type="submit" style="background: linear-gradient(to right, #e52d27, #b31217);" value="Yes, delete my account">
                <a href="edit.jsp">No, take me back</a>
            </div>
        </form>
    </div>
</body>
</html>