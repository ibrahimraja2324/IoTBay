<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="jakarta.servlet.http.*, jakarta.servlet.*" %>

<%
    String message = (String) session.getAttribute("orderConfirmationMessage");
    if (message == null || message.trim().isEmpty()) {
        message = "Your order has been successfully placed.";
    }
    session.removeAttribute("orderConfirmationMessage");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Order Confirmation – IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <style>
    body {
      font-family: sans-serif;
      background: #f4f4f4;
      margin: 0;
      padding-bottom: 100px; 
    }
    .page-nav {
      background-color: #333;
      color: #fff;
      padding: 12px 24px;
      display: flex;
      justify-content: space-between;
    }
    .page-nav a {
      color: #fff;
      text-decoration: none;
      margin: 0 10px;
    }
    .main-box {
      max-width: 600px;
      margin: 60px auto;
      background: white;
      padding: 40px;
      border-radius: 10px;
      text-align: center;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
    .main-box h1 {
      color: black; 
    }
    .btn {
      display: inline-block;
      margin-top: 24px;
      padding: 10px 20px;
      background-color: #007BFF;
      color: white;
      text-decoration: none;
      border-radius: 6px;
    }
    .bottom-nav {
      position: fixed;
      bottom: 0;
      width: 100%;
      background-color: #f9f9f9;
      padding: 12px 0;
      text-align: center;
      box-shadow: 0 -2px 8px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body>

<nav class="page-nav">
  <div class="nav-left">
    <a href="index.jsp">Home</a>
  </div>
  <div class="nav-right">
    <a href="logout.jsp">Logout</a>
  </div>
</nav>

<div class="main-box">
  <h1>Thank You!</h1>
  <p><%= message %></p>

  <a class="btn" href="orderlist.jsp">View my orders</a> 
</div>

<div class="bottom-nav">
  <a href="index.jsp" class="btn">← Return to Home</a>
</div>

</body>
</html>