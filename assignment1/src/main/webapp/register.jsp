<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String registerError = (String) session.getAttribute("registerError");
    String emailError    = (String) session.getAttribute("emailError");
    String passwordError = (String) session.getAttribute("passwordError");
    String nameError     = (String) session.getAttribute("nameError");
    String phoneError    = (String) session.getAttribute("phoneError");

    session.removeAttribute("registerError");
    session.removeAttribute("emailError");
    session.removeAttribute("passwordError");
    session.removeAttribute("nameError");
    session.removeAttribute("phoneError");
%>
<!DOCTYPE html>
<html lang="en" id="register-page">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - IoTBay</title>
    <link rel="stylesheet" href="style.css">
    </style>
</head>
<body>
    <nav class="page-nav">
        <div class="nav-left">
            <a href="main.jsp">Home</a>
        </div>
    </nav>
    
    <form class="login-box" action="RegisterServlet" method="post">
        <h2>Create Account</h2>
        
        <%
            if (registerError != null || emailError != null || passwordError != null || nameError != null || phoneError != null) {
        %>
            <div class="error-messages">
                <%
                    if (registerError != null) { %>
                        <p><%= registerError %></p>
                <% }
                    if (emailError != null) { %>
                        <p><%= emailError %></p>
                <% }
                    if (passwordError != null) { %>
                        <p><%= passwordError %></p>
                <% }
                    if (nameError != null) { %>
                        <p><%= nameError %></p>
                <% }
                    if (phoneError != null) { %>
                        <p><%= phoneError %></p>
                <% }
                %>
            </div>
        <%
            }
        %>
        
        <label class="heading" for="firstName">First Name</label>
        <input type="text" name="firstName" id="firstName" placeholder="First Name" 
               value="<%= request.getParameter("firstName") != null ? request.getParameter("firstName") : "" %>" required>
        <label class="heading" for="lastName">Last Name</label>
        <input type="text" name="lastName" id="lastName" placeholder="Last Name" 
               value="<%= request.getParameter("lastName") != null ? request.getParameter("lastName") : "" %>" required>
        <label class="heading" for="email">Email</label>
        <input type="text" name="email" id="email" placeholder="Email" 
               value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
        <label class="heading" for="password">Password</label>
        <input type="password" name="password" id="password" placeholder="Password" required>
        <label class="heading" for="confirmPassword">Confirm Password</label>
        <input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm Password" required>
        <label class="heading" for="phone">Phone Number</label>
        <input type="text" name="phone" id="phone" placeholder="Phone Number" 
               value="<%= request.getParameter("phone") != null ? request.getParameter("phone") : "" %>" required>
        <input type="submit" value="Register">
        <a href="login.jsp">Already have an account? Login</a>
    </form>
</body>
</html>
