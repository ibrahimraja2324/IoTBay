<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Shipment Management - IoTBay</title>
  <link rel="stylesheet" href="shipment-style.css">
</head>
<body>
  <!-- Navigation Bar -->
  <nav class="page-nav">
    <div class="nav-left">
      <a href="main.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="payment-dashboard.jsp">Payments</a>
      <a href="edit.jsp">Profile</a>
      <a href="LogoutServlet">Logout</a>
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
          <input type="date" name="searchTerm" id="dateSearch" class="d-none">
          
          <button type="submit">Search</button>
        </form>
      </div>
    </div>
    
    <div class="dashboard-section">
      <h2>📦 Your Shipments</h2>
      <iframe src="ShipmentServlet" class="shipment-list-iframe"></iframe>
    </div>
  </div>

  <script>
    function toggleSearchInput() {
      var searchBy = document.getElementById('searchBy').value;
      var textSearch = document.getElementById('textSearch');
      var dateSearch = document.getElementById('dateSearch');
      
      if (searchBy === 'date') {
        textSearch.classList.add('d-none');
        dateSearch.classList.remove('d-none');
        textSearch.name = '';
        dateSearch.name = 'searchTerm';
      } else {
        textSearch.classList.remove('d-none');
        dateSearch.classList.add('d-none');
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