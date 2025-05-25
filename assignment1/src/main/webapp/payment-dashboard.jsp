<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Payment Dashboard - IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <script>
    function formatCardNumber(input) {
      var digits = input.value.replace(/\D/g, '');
      if (digits.length > 16) {
         digits = digits.substring(0, 16);
      }
      var formatted = "";
      for (var i = 0; i < digits.length; i++) {
          if (i > 0 && i % 4 === 0) {
              formatted += "-";
          }
          formatted += digits[i];
      }
      input.value = formatted;
    }
  </script>
</head>
<body>
  <nav class="page-nav">
    <div class="nav-left">
      <a href="main.jsp">Home</a>
    </div>
    <div class="nav-right">
      <a href="edit.jsp">Edit</a>
      <a href="LogoutServlet">Logout</a>
    </div>
  </nav>
  
  <h1 class="dashboard-title">Payment Dashboard</h1>
  
  <div class="dashboard-container">
    <div class="payment-form-container">
      <h2>Add New Payment Method</h2>
      <form action="PaymentMethodServlet?action=add" method="post">
        <label for="paymentMethod">Payment Method:</label>
        <% if (request.getAttribute("paymentMethodError") != null) { %>
          <div class="error"><%= request.getAttribute("paymentMethodError") %></div>
        <% } %>
        <select id="paymentMethod" name="paymentMethod" required>
          <option value="">-- Select Payment Method --</option>
          <option value="Visa" <%= "Visa".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>Visa</option>
          <option value="MasterCard" <%= "MasterCard".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>MasterCard</option>
          <option value="American Express" <%= "American Express".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>American Express</option>
          <option value="Discover" <%= "Discover".equals(request.getAttribute("paymentMethodInput")) ? "selected" : "" %>>Discover</option>
        </select>
        
        <label for="cardHolderName">Card Holder Name:</label>
        <% if (request.getAttribute("cardHolderNameError") != null) { %>
          <div class="error"><%= request.getAttribute("cardHolderNameError") %></div>
        <% } %>
        <input type="text" id="cardHolderName" name="cardHolderName" placeholder="Full Name"
               value="<%= request.getAttribute("cardHolderNameInput") != null ? request.getAttribute("cardHolderNameInput") : "" %>" required>
        
        <label for="cardNumber">Card Number:</label>
        <% if (request.getAttribute("cardNumberError") != null) { %>
          <div class="error"><%= request.getAttribute("cardNumberError") %></div>
        <% } %>
        <input type="text" id="cardNumber" name="cardNumber" placeholder="1234-5678-9012-3456"
               oninput="formatCardNumber(this)" 
               value="<%= request.getAttribute("cardNumberInput") != null ? request.getAttribute("cardNumberInput") : "" %>" required>

        <label for="cvv">CVV:</label>
        <% if (request.getAttribute("cvvError") != null) { %>
          <div class="error"><%= request.getAttribute("cvvError") %></div>
        <% } %>
        <input type="text" id="cvv" name="cvv" placeholder="3 or 4-digit CVV" maxlength="4"
               value="<%= request.getAttribute("cvvInput") != null ? request.getAttribute("cvvInput") : "" %>" required>
        
        <label for="expiryDate">Expiry Date:</label>
        <% if (request.getAttribute("expiryDateError") != null) { %>
          <div class="error"><%= request.getAttribute("expiryDateError") %></div>
        <% } %>
        
        <input id="expiry-date-unique-id" type="month" id="expiryDate" name="expiryDate"
               value="<%= request.getAttribute("expiryDateInput") != null ? request.getAttribute("expiryDateInput") : "" %>" required>
        
        <input type="submit" value="Save Payment Method">
      </form>
    </div>
    
    <div class="table-container">
      <h2>Your Payment Methods</h2>
      <iframe src="PaymentMethodServlet?action=list" class="list-iframe"></iframe>
    </div>
    
    <div class="table-container">
      <h2>Order History</h2>
      <iframe src="OrderHistoryServlet" class="list-iframe"></iframe>
    </div>
  </div>
</body>
</html>
