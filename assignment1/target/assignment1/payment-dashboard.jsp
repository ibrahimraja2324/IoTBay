<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Payment Dashboard - IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <style>
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
    .toggle-link {
      display: block;
      text-align: center;
      color: #2575fc;
      text-decoration: underline;
      cursor: pointer;
      margin-bottom: 15px;
    }
    .new-payment-form {
      display: none;
      width: 400px;
      margin: 0 auto 20px auto;
      background: #f9f9f9;
      border: 1px solid #ccc;
      padding: 20px;
      border-radius: 8px;
    }
    .new-payment-form label {
      display: block;
      margin: 10px 0 5px 0;
    }
    .new-payment-form input[type="text"],
    .new-payment-form input[type="date"],
    .new-payment-form select {
      width: 100%;
      padding: 8px;
      margin-bottom: 5px;
      box-sizing: border-box;
    }
    .new-payment-form input[type="submit"] {
      width: 100%;
      padding: 10px;
      background-color: #2575fc;
      border: none;
      color: #fff;
      cursor: pointer;
    }
    .error {
      color: red;
      font-size: 0.9em;
      margin-bottom: 5px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin: 0 auto;
    }
    th, td {
      padding: 10px;
      border: 1px solid #ccc;
      text-align: center;
    }
    th {
      background: #f0f0f0;
    }
  </style>
  <script>
    function togglePaymentMethodForm(){
      var form = document.getElementById("newPaymentMethodForm");
      if(form.style.display === "none" || form.style.display === ""){
        form.style.display = "block";
      } else {
        form.style.display = "none";
      }
    }
  </script>
</head>
<body>
  <nav class="page-nav">
    <div class="nav-left">
      <a href="index.jsp">Home</a>
      <a href="main.jsp">Main</a>
    </div>
    <div class="nav-right">
      <a href="edit.jsp">Edit</a>
      <a href="logout.jsp">Logout</a>
    </div>
  </nav>
  
  <h1 style="text-align:center;">Payment Dashboard</h1>
  
  <div class="dashboard-container">
    <div class="dashboard-section">
      <h2>Your Payment Methods</h2>
      <span class="toggle-link" onclick="togglePaymentMethodForm()">New Payment Method</span>
      <div id="newPaymentMethodForm" class="new-payment-form">
        <form action="PaymentMethodServlet?action=add" method="post">

          <!-- Payment Method -->
          <label for="paymentMethod">Payment Method:</label>
          <% if(request.getAttribute("paymentMethodError") != null){ %>
              <div class="error"><%= request.getAttribute("paymentMethodError") %></div>
          <% } %>
          <select id="paymentMethod" name="paymentMethod" required>
            <option value="">-- Select Payment Method --</option>
            <option value="Visa" <%= "Visa".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>Visa</option>
            <option value="MasterCard" <%= "MasterCard".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>MasterCard</option>
            <option value="American Express" <%= "American Express".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>American Express</option>
            <option value="Discover" <%= "Discover".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>Discover</option>
          </select>
          <br><br>
          
          <!-- Card Holder Name -->
          <label for="cardHolderName">Card Holder Name:</label>
          <% if(request.getAttribute("cardHolderNameError") != null){ %>
              <div class="error"><%= request.getAttribute("cardHolderNameError") %></div>
          <% } %>
          <input type="text" id="cardHolderName" name="cardHolderName" placeholder="Full Name" 
                 value="<%= request.getAttribute("cardHolderNameInput") != null ? request.getAttribute("cardHolderNameInput") : "" %>" required>
          <br><br>
          
          <!-- Card Number -->
          <label for="cardNumber">Card Number:</label>
          <% if(request.getAttribute("cardNumberError") != null){ %>
              <div class="error"><%= request.getAttribute("cardNumberError") %></div>
          <% } %>
          <input type="text" id="cardNumber" name="cardNumber" placeholder="16-digit card number" 
                 value="<%= request.getAttribute("cardNumberInput") != null ? request.getAttribute("cardNumberInput") : "" %>" required>
          <br><br>
          
          <!-- CVV -->
          <label for="cvv">CVV:</label>
          <% if(request.getAttribute("cvvError") != null){ %>
              <div class="error"><%= request.getAttribute("cvvError") %></div>
          <% } %>
          <input type="text" id="cvv" name="cvv" placeholder="3-digit CVV" 
                 value="<%= request.getAttribute("cvvInput") != null ? request.getAttribute("cvvInput") : "" %>" required>
          <br><br>
          
          <!-- Expiry Date -->
          <label for="expiryDate">Expiry Date:</label>
          <% if(request.getAttribute("expiryDateError") != null){ %>
              <div class="error"><%= request.getAttribute("expiryDateError") %></div>
          <% } %>
          <input type="date" id="expiryDate" name="expiryDate" 
                 value="<%= request.getAttribute("expiryDateInput") != null ? request.getAttribute("expiryDateInput") : "" %>" required>
          <br><br>
          
          <input type="submit" value="Save Payment Method">
        </form>
      </div>
      
      <iframe src="PaymentMethodServlet?action=list" style="width:100%; height:300px; border:none;"></iframe>
    </div>

    <div class="dashboard-section">
      <h2>Order History</h2>
      <iframe src="OrderHistoryServlet" style="width:100%; height:300px; border:none;"></iframe>
    </div>
    
  </div>
  
</body>
</html>
