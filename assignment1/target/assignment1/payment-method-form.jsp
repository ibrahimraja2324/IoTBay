<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Edit Payment Method - IoTBay</title>
  <link rel="stylesheet" href="style.css">
  <script>
    function formatCardNumber(input) {
      var digits = input.value.replace(/\D/g, '');
      if(digits.length > 16) {
         digits = digits.substring(0, 16);
      }
      var formatted = "";
      for(var i = 0; i < digits.length; i++) {
         if(i > 0 && i % 4 === 0){
            formatted += "-";
         }
         formatted += digits[i];
      }
      input.value = formatted;
    }
  </script>
</head>
<body>
  <main class="content-area">
    <h1 class="page-title">Edit Payment Method</h1>
    
    <div class="edit-payment-form-container">
      <%
         iotbay.model.Payment paymentMethod = (iotbay.model.Payment) request.getAttribute("paymentMethod");
         if(paymentMethod == null) {
      %>
         <p class="no-payment-error">Payment method not found.</p>
      <%
         } else {
      %>
      <form action="PaymentMethodServlet?action=update" method="post" class="edit-payment-form">
         <input type="hidden" name="paymentId" value="<%= paymentMethod.getPaymentId() %>">
         
         <div class="edit-payment-form-group">
           <label for="paymentMethod">Payment Method:</label>
           <% if(request.getAttribute("paymentMethodError") != null) { %>
             <div class="error-message"><%= request.getAttribute("paymentMethodError") %></div>
           <% } %>
           <select id="paymentMethod" name="paymentMethod" required>
             <option value="Visa" <%= "Visa".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>Visa</option>
             <option value="MasterCard" <%= "MasterCard".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>MasterCard</option>
             <option value="American Express" <%= "American Express".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>American Express</option>
             <option value="Discover" <%= "Discover".equals(paymentMethod.getPaymentMethod()) ? "selected" : "" %>>Discover</option>
           </select>
         </div>
         
         <div class="edit-payment-form-group">
           <label for="cardHolderName">Card Holder Name:</label>
           <% if(request.getAttribute("cardHolderNameError") != null) { %>
             <div class="error-message"><%= request.getAttribute("cardHolderNameError") %></div>
           <% } %>
           <input type="text" id="cardHolderName" name="cardHolderName" 
                  value="<%= paymentMethod.getCardHolderName() != null ? paymentMethod.getCardHolderName() : "" %>" required>
         </div>
         <div class="edit-payment-form-group">
           <label for="cardNumber">Card Number:</label>
           <% if(request.getAttribute("cardNumberError") != null) { %>
             <div class="error-message"><%= request.getAttribute("cardNumberError") %></div>
           <% } %>
           <input type="text" id="cardNumber" name="cardNumber" 
                  value="<%= paymentMethod.getCardNumber() != null ? paymentMethod.getCardNumber() : "" %>" 
                  oninput="formatCardNumber(this)" required>
         </div>
         <div class="edit-payment-form-group">
           <label for="cvv">CVV:</label>
           <% if(request.getAttribute("cvvError") != null) { %>
             <div class="error-message"><%= request.getAttribute("cvvError") %></div>
           <% } %>
           <input type="text" id="cvv" name="cvv" maxlength="4"
                  value="<%= paymentMethod.getCvv() != null ? paymentMethod.getCvv() : "" %>" required>
         </div>
         <div class="edit-payment-form-group">
           <label for="expiryDate">Expiry Date:</label>
           <% if(request.getAttribute("expiryDateError") != null) { %>
             <div class="error-message"><%= request.getAttribute("expiryDateError") %></div>
           <% } %>
           <input type="date" id="expiryDate" name="expiryDate"
                  value="<%= paymentMethod.getExpiryDate() != null ? paymentMethod.getExpiryDate() : "" %>" required>
         </div>
         
         <div class="edit-payment-form-group">
           <input type="submit" value="Update Payment Method" class="submit-btn">
         </div>
      </form>
      <% } %>
    </div>
  </main>
</body>
</html>
