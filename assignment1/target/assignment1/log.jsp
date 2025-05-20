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
        <ul>
            <li><a href="index.jsp">Home</a></li>
        </ul>
    </nav>
    <div class="container">
        <div class="index-content">
            <h1>Logs</h1>
            <table>
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
        </div>
    </div>
</body>
</html>