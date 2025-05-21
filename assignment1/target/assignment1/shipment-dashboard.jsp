<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shipment Management - IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <style>
    /* Dashboard container styling */
    .dashboard-container {
      margin: 20px;
    }
    .dashboard-section {
      margin-bottom: 40px;
    }
    .dashboard-section h2 {
      text-align: center;
      margin-bottom: 15px;
    }
    /* Status indicators */
    .status-pending {
      background-color: #ffc107;
      padding: 4px 8px;
      border-radius: 4px;
      color: #000;
    }
    .status-processing {
      background-color: #17a2b8;
      padding: 4px 8px;
      border-radius: 4px;
      color: #fff;
    }
    .status-shipped {
      background-color: #28a745;
      padding: 4px 8px;
      border-radius: 4px;
      color: #fff;
    }
    .status-delivered {
      background-color: #6c757d;
      padding: 4px 8px;
      border-radius: 4px;
      color: #fff;
    }
    .search-container {
      margin-bottom: 20px;
      text-align: center;
    }
    .search-container form {
      display: inline-flex;
      align-items: center;
      gap: 10px;
    }
    .message-box {
      padding: 10px 15px;
      margin-bottom: 20px;
      border-radius: 5px;
      text-align: center;
    }
    .success-message {
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }
    .error-message {
      background-color: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }
    .action-button {
      padding: 10px 20px;
      border-radius: 5px;
      text-decoration: none;
      display: inline-block;
      margin-right: 10px;
      text-align: center;
    }
    .btn-add {
      background-color: #28a745;
      color: white;
    }
    .btn-add:hover {
      background-color: #218838;
    }
  </style>
</head>
<body>
  <!-- NavBar -->
  <nav class="page-nav">
    <div class="nav-left">
      <a href="index.jsp">Home</a>
      <a href="main.jsp">Main</a>
    </div>
    <div class="nav-right">
      <a href="payment-dashboard.jsp">Payments</a>
      <a href="edit.jsp">Profile</a>
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>
  
  <h1 style="text-align:center;">Shipment Management</h1>
  
  <div class="dashboard-container">
    <%
      // Display success or error messages if any
      String successMessage = (String) session.getAttribute("successMessage");
      String shipmentError = (String) session.getAttribute("shipmentError");
      
      // Clear the messages from session after displaying
      session.removeAttribute("successMessage");
      session.removeAttribute("shipmentError");
      
      if (successMessage != null) {
    %>
    <div class="message-box success-message">
      <%= successMessage %>
    </div>
    <%
      }
      
      if (shipmentError != null) {
    %>
    <div class="message-box error-message">
      <%= shipmentError %>
    </div>
    <%
      }
    %>

    <div class="dashboard-section">
      <div style="text-align: center; margin-bottom: 20px;">
        <a href="ShipmentServlet?action=create" class="action-button btn-add">Create New Shipment</a>
      </div>
      
      <h2>Search Shipments</h2>
      <div class="search-container">
        <form action="ShipmentServlet" method="get">
          <input type="hidden" name="action" value="search">
          <select name="searchBy" style="padding: 8px; border-radius: 4px;">
            <option value="id">Shipment ID</option>
            <option value="date">Shipment Date</option>
          </select>
          <input type="text" name="searchTerm" placeholder="Search term..." style="padding: 8px; border-radius: 4px; width: 200px;">
          <button type="submit" style="padding: 8px 16px; border-radius: 4px; background-color: #2575fc; color: white; border: none; cursor: pointer;">Search</button>
        </form>
      </div>
      
      <h2>Your Shipments</h2>
      <iframe src="ShipmentServlet" style="width:100%; height:400px; border:none;"></iframe>
    </div>
  </div>
</body>
</html>