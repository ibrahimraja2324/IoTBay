<%@ page import="java.util.List" %>
<%@ page import="iotbay.model.Log" %>
<%
    String logError = (String) request.getAttribute("logError");
    session.removeAttribute("logError");
%>

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
    <div class="container">
        <div class="index-content">
            <h1>Logs</h1>
            <div class="log-filter-bar">
                <form method="get" action="LogServlet" style="display: flex; align-items: center; gap: 12px;">
                    <label for="searchDate" style="font-size: 18px; margin-right: 12px;">Search by Date:</label>
                    <select name="dateFilter" style="font-size: 17px;">
                        <option value="on" <%= "on".equals(request.getParameter("dateFilter")) ? "selected" : "" %>>On</option>
                        <option value="before" <%= "before".equals(request.getParameter("dateFilter")) ? "selected" : "" %>>Before</option>
                        <option value="after" <%= "after".equals(request.getParameter("dateFilter")) ? "selected" : "" %>>After</option>
                    </select>
                    <input type="date" id="searchDate" name="searchDate" value="<%= request.getParameter("searchDate") != null ? request.getParameter("searchDate") : "" %>">
                    <button type="submit" class="btn-primary">Search</button>
                </form>
            </div>

            <%
                if (logError != null) { %>
                <div class="error-messages">
                    <p><%= logError %></p>
                </div>
            <% } else { %>
                <table class="user-details wide">
                    <thead>
                        <tr>
                            <th>Log ID</th>
                            <th>Email</th>
                            <th>Action</th>
                            <th>Role</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Log> logs = (List<Log>) request.getAttribute("logs");
                            if (logs != null && !logs.isEmpty()) {
                                for (Log log : logs) {
                        %>
                        <tr>
                            <td><%= log.getLogID() %></td>
                            <td><%= log.getEmail() %></td>
                            <td><%= log.getAction() %></td>
                            <td><%= log.getRole() %></td>
                            <td><%= log.getTime() %></td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="5">No logs available.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            <% } %>
        </div>
    </div>
</body>
</html>