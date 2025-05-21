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
    .container {
      max-width: 1000px;
      margin: 100px auto 50px auto;
      padding: 0 20px;
    }
    
    .page-title {
      text-align: center;
      color: #fff;
      margin-bottom: 30px;
    }
    
    .card {
      background-color: #28243c;
      border-radius: 8px;
      padding: 25px;
      margin-bottom: 30px;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    }
    
    .card-title {
      color: #a6a6ff;
      text-align: center;
      margin-bottom: 20px;
      font-weight: 600;
    }
    
    .message {
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
      text-align: center;
    }
    
    .message.success {
      background-color: rgba(40, 167, 69, 0.2);
      border: 1px solid rgba(40, 167, 69, 0.4);
      color: #28a745;
    }
    
    .message.error {
      background-color: rgba(220, 53, 69, 0.2);
      border: 1px solid rgba(220, 53, 69, 0.4);
      color: #dc3545;
    }
    
    .btn-create {
      display: block;
      width: 250px;
      margin: 0 auto 25px auto;
      background: linear-gradient(to right, #2575fc, #6a11cb);
      color: white;
      padding: 12px 24px;
      border-radius: 6px;
      text-decoration: none;
      font-weight: 500;
      text-align: center;
      transition: all 0.3s ease;
    }
    
    .btn-create:hover {
      background: linear-gradient(to right, #6a11cb, #2575fc);
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(37, 117, 252, 0.3);
    }
    
    .search-form {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 10px;
      margin-bottom: 20px;
    }
    
    .search-form select,
    .search-form input[type="text"] {
      padding: 10px;
      border: none;
      border-radius: 4px;
      background-color: #3a3760;
      color: #fff;
    }
    
    .search-form button {
      background: linear-gradient(to right, #2575fc, #6a11cb);
      color: white;
      border: none;
      padding: 10px 20px;
      border-radius: 4px;
      cursor: pointer;
    }
    
    .shipment-list-frame {
      width: 100%;
      height: 500px;
      border: none;
      border-radius: 8px;
      background-color: #1f1c2c;
      margin-top: 20px;
    }
    
    @media (max-width: 768px) {
      .search-form {
        flex-direction: column;
        align-items: center;
      }
      
      .search-form select,
      .search-form input[type="text"],
      .search-form button {
        width: 100%;
      }
    }
  </style>
</head>
<body>
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
  
  <div class="container">
    <h1 class="page-title">Shipment Management</h1>
    
    <%
      // Display success or error messages if any
      String successMessage = (String) session.getAttribute("successMessage");
      String shipmentError = (String) session.getAttribute("shipmentError");
      
      // Clear the messages from session after displaying
      session.removeAttribute("successMessage");
      session.removeAttribute("shipmentError");
      
      if (successMessage != null) {
    %>
    <div class="message success">
      <%= successMessage %>
    </div>
    <%
      }
      
      if (shipmentError != null) {
    %>
    <div class="message error">
      <%= shipmentError %>
    </div>
    <%
      }
    %>
    
    <div class="card">
      <a href="ShipmentServlet?action=create" class="btn-create">Create New Shipment</a>
      
      <h2 class="card-title">Search Shipments</h2>
      <form action="ShipmentServlet" method="get" class="search-form">
        <input type="hidden" name="action" value="search">
        <select name="searchBy">
          <option value="id">Shipment ID</option>
          <option value="date">Shipment Date</option>
        </select>
        <input type="text" name="searchTerm" placeholder="Search term...">
        <button type="submit">Search</button>
      </form>
      
      <!-- The iframe with target="_parent" for all links -->
      <iframe src="ShipmentServlet" class="shipment-list-frame" name="shipment-list"></iframe>
    </div>
  </div>
</body>
</html>