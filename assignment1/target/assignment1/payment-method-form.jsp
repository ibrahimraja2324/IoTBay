<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Payment Method</title>
</head>
<body>
    <h1>Edit Payment Method</h1>
    <%
        // Retrieve the payment to update.
        iotbay.model.Payment paymentMethod = (iotbay.model.Payment) request.getAttribute("paymentMethod");
        if (paymentMethod == null) {
    %>
        <p>Payment method not found.</p>
    <%
        } else {
    %>
    <form action="PaymentMethodServlet?action=update" method="post">
        <input type="hidden" name="paymentId" value="<%= paymentMethod.getPaymentId() %>">
        
        <!-- Payment Method -->
        <label for="paymentMethod">Payment Method:</label>
        <% if(request.getAttribute("paymentMethodError") != null){ %>
          <div class="error"><%= request.getAttribute("paymentMethodError") %></div>
        <% } %>
        <select id="paymentMethod" name="paymentMethod" required>
          <option value="Visa" <%= "Visa".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>Visa</option>
          <option value="MasterCard" <%= "MasterCard".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>MasterCard</option>
          <option value="American Express" <%= "American Express".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>American Express</option>
          <option value="Discover" <%= "Discover".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>Discover</option>
        </select>
        <br><br>
        
        <!-- Card Holder Name -->
        <label for="cardHolderName">Card Holder Name:</label>
        <% if(request.getAttribute("cardHolderNameError") != null){ %>
          <div class="error"><%= request.getAttribute("cardHolderNameError") %></div>
        <% } %>
        <input type="text" id="cardHolderName" name="cardHolderName" 
               value="<%= paymentMethod.getCardHolderName() != null ? paymentMethod.getCardHolderName() : "" %>" required>
        <br><br>
        
        <!-- Card Number -->
        <label for="cardNumber">Card Number:</label>
        <% if(request.getAttribute("cardNumberError") != null){ %>
          <div class="error"><%= request.getAttribute("cardNumberError") %></div>
        <% } %>
        <input type="text" id="cardNumber" name="cardNumber" 
               value="<%= paymentMethod.getCardNumber() != null ? paymentMethod.getCardNumber() : "" %>" required>
        <br><br>
        
        <!-- CVV -->
        <label for="cvv">CVV:</label>
        <% if(request.getAttribute("cvvError") != null){ %>
          <div class="error"><%= request.getAttribute("cvvError") %></div>
        <% } %>
        <input type="text" id="cvv" name="cvv" 
               value="<%= paymentMethod.getCvv() != null ? paymentMethod.getCvv() : "" %>" required>
        <br><br>
        
        <!-- Expiry Date -->
        <label for="expiryDate">Expiry Date:</label>
        <% if(request.getAttribute("expiryDateError") != null){ %>
          <div class="error"><%= request.getAttribute("expiryDateError") %></div>
        <% } %>
        <input type="date" id="expiryDate" name="expiryDate" 
               value="<%= paymentMethod.getExpiryDate() != null ? paymentMethod.getExpiryDate() : "" %>" required>
        <br><br>
        
        <input type="submit" value="Update Payment Method">
    </form>
    <%
        }
    %>
</body>
</html>
