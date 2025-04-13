<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Edit Profile - IoTBay</title>
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
      <a href="main.jsp">Main</a>
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>
  
  <div id="edit-page">
    <div class="edit-box">
      <%
         User user = (User) session.getAttribute("currentUser");
         boolean submitted = false;

         if(request.getParameter("name") != null) {
             user.setName(request.getParameter("name"));
             user.setPhone(request.getParameter("phone"));
             user.setEmailAddress(request.getParameter("email"));
             user.setPassword(request.getParameter("password"));
             
             session.setAttribute("currentUser", user);
             submitted = true;
         }
      %>
      
      <% if(submitted) { %>
         <h2>Profile Updated!</h2>
         <p>Your profile has been updated successfully.</p>
         <p>
           <a href="main.jsp" class="btn-main">Return to Main</a>
         </p>
      <% } else { %>
         <h2>Edit Your Profile</h2>
         <form class="edit-form" action="edit.jsp" method="post">
             <label>Full Name:</label>
             <input type="text" name="name" value="<%= user.getName() != null ? user.getName() : "" %>" required>
             
             <label>Phone Number:</label>
             <input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>" required>
             
             <label>Email:</label>
             <input type="text" name="email" value="<%= user.getEmailAddress() != null ? user.getEmailAddress() : "" %>" required>
             
             <label>Password:</label>
             <input type="password" name="password" value="<%= user.getPassword() != null ? user.getPassword() : "" %>" required>
             
             <label>Confirm Password:</label>
             <input type="password" name="confirmPassword" value="<%= user.getPassword() != null ? user.getPassword() : "" %>" required>
             
             <input type="submit" value="Save Changes">
             <a href="main.jsp" class="btn-cancel">Cancel</a>
         </form>
      <% } %>
    </div>
  </div>
  
</body>
</html>
