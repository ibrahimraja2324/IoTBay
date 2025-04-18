<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Main - IoTBay</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
 
  <nav class="page-nav">
    <div class="nav-left">
      <a href="index.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="edit.jsp">Edit</a>
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>
 
  <div id="main-page">
    <div class="main-box">
      <%
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser != null) {
      %>
        <h2>Welcome, <%= currentUser.getName() != null ? currentUser.getName() : currentUser.getEmailAddress() %>!</h2>
        <p>Your details are as follows:</p>
        <table class="user-details">
           <tr>
             <th>Field</th>
             <th>Value</th>
           </tr>
           <tr>
             <td>User ID</td>
             <td><%= currentUser.getUserID() %></td>
           </tr>
           <tr>
             <td>Full Name</td>
             <td><%= currentUser.getName() %></td>
           </tr>
           <tr>
             <td>Email</td>
             <td><%= currentUser.getEmailAddress() %></td>
           </tr>
           <tr>
             <td>Phone</td>
             <td><%= currentUser.getPhone() %></td>
           </tr>
           <tr>
             <td>Password</td>
             <td><%= currentUser.getPassword() %></td>
           </tr>
        </table>
      <%
        } else {
      %>
        <p>No user information is available. Please <a href="login.jsp">log in</a> again.</p>
      <%
        }
      %>
    </div>
  </div>
  
</body>
</html>
