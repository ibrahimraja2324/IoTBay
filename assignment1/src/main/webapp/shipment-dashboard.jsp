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
    .dashboard-container {
      max-width: 900px;
      margin: 120px auto 50px auto;
    }
    
    .dashboard-title {
      text-align: center;
      margin-bottom: 30px;
      color: #fff;
    }
    
    .dashboard-section {
      margin-bottom: 40px;
      background: #28243c;
      border-radius: 12px;
      padding: 25px;
      box-shadow: 0 2px 16px rgba(30, 22, 54, 0.25);
    }
    
    .dashboard-section h2 {
      text-align: center;
      margin-bottom: 20px;
      color: #a6a6ff;
      font-weight: 600;
    }
    
    /* Status indicators */
    .status-badge {
      display: inline-block;
      padding: 6px 12px;
      border-radius: 4px;
      font-weight: 500;
    }
    
    .status-pending {
      background-color: #ffc107;
      color: #000;
    }
    
    .status-processing {
      background-color: #17a2b8;
      color: #fff;
    }
    
    .status-shipped {
      background-color: #28a745;
      color: #fff;
    }
    
    .status-delivered {
      background-color: #6c757d;
      color: #fff;
    }
    
    /* Search section styling */
    .search-container {
      margin-bottom: 20px;
      text-align: center;
    }
    
    .search-container form {
      display: inline-flex;
      align-items: center;
      flex-wrap: wrap;
      justify-content: center;
      gap: 10px;
    }
    
    .search-container select,
    .search-container input[type="text"],
    .search-container input[type="date"] {
      padding: 10px;
      border: none;
      border-radius: 4px;
      background-color: #3a3760;
      color: #fff;
      font-size: 16px;
      font-family: 'Quicksand', sans-serif;
    }
    
    .search-container input[type="date"] {
      color-scheme: dark;
    }
    
    .search-container input[type="date"]::-webkit-calendar-picker-indicator {
      filter: invert(1);
      cursor: pointer;
    }
    
    .search-container button {
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      background: linear-gradient(to right, #2575fc, #6a11cb);
      color: white;
      cursor: pointer;
      font-size: 16px;
      transition: all 0.3s ease;
    }
    
    .search-container button:hover {
      background: linear-gradient(to right, #6a11cb, #2575fc);
    }
    
    /* Message boxes */
    .message-box {
      padding: 15px;
      margin-bottom: 20px;
      border-radius: 8px;
      text-align: center;
    }
    
    .success-message {
      background-color: rgba(40, 167, 69, 0.2);
      color: #28a745;
      border: 1px solid rgba(40, 167, 69, 0.3);
    }
    
    .error-message {
      background-color: rgba(220, 53, 69, 0.2);
      color: #dc3545;
      border: 1px solid rgba(220, 53, 69, 0.3);
    }
    
    /* Action buttons */
    .action-container {
      text-align: center;
      margin-bottom: 25px;
    }
    
    .btn-add-shipment {
      display: inline-block;
      padding: 12px 24px;
      background: linear-gradient(to right, #2575fc, #6a11cb);
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      text-decoration: none;
      cursor: pointer;
      transition: transform 0.2s;
    }
    
    .btn-add-shipment:hover {
      transform: translateY(-2px);
      background: linear-gradient(to right, #6a11cb, #2575fc);
    }
    
    /* List iframe */
    .shipment-list-iframe {
      width: 100%;
      height: 400px;
      border: none;
      border-radius: 8px;
      background-color: #1f1c2c;
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
  
  <div class="dashboard-container">
    <h1 class="dashboard-title">Shipment Management</h1>
    
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
      <div class="action-container">
        <a href="ShipmentServlet?action=create" class="btn-add-shipment">Create New Shipment</a>
      </div>
      
      <h2>Search Shipments</h2>
      <div class="search-container">
        <form action="ShipmentServlet" method="get">
          <input type="hidden" name="action" value="search">
          <select name="searchBy" id="searchBy" onchange="toggleSearchInput()">
            <option value="id">Shipment ID</option>
            <option value="date">Shipment Date</option>
          </select>
          
          <!-- Text input for Shipment ID -->
          <input type="text" name="searchTerm" id="textSearch" placeholder="Enter Shipment ID...">
          
          <!-- Date input for Shipment Date -->
          <input type="date" name="searchTerm" id="dateSearch" style="display: none;">
          
          <button type="submit">Search</button>
        </form>
      </div>
    </div>
    
    <div class="dashboard-section">
      <h2>Your Shipments</h2>
      <iframe src="ShipmentServlet" class="shipment-list-iframe"></iframe>
    </div>
  </div>

  <script>
    function toggleSearchInput() {
      var searchBy = document.getElementById('searchBy').value;
      var textSearch = document.getElementById('textSearch');
      var dateSearch = document.getElementById('dateSearch');
      
      if (searchBy === 'date') {
        textSearch.style.display = 'none';
        dateSearch.style.display = 'inline-block';
        textSearch.name = '';
        dateSearch.name = 'searchTerm';
      } else {
        textSearch.style.display = 'inline-block';
        dateSearch.style.display = 'none';
        textSearch.name = 'searchTerm';
        dateSearch.name = '';
      }
    }

    // Initialize the form on page load
    document.addEventListener('DOMContentLoaded', function() {
      toggleSearchInput();
    });
  </script>
</body>
</html>