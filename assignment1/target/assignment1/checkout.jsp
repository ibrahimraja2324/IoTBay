<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Cart" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    Cart cart = (Cart) session.getAttribute("cart");
    double totalAmount = (cart != null) ? cart.getTotalPrice() : 0.0;
    DecimalFormat df = new DecimalFormat("0.00");

    String error = (String) session.getAttribute("checkoutError");
    if (error != null) {
        session.removeAttribute("checkoutError");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Checkout – IoTBay</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css?v=<%= System.currentTimeMillis() %>">
  
  <style>
    .page-nav {
      background-color: #333;
      color: #fff;
      padding: 12px 24px;
      display: flex;
      justify-content: space-between;
    }
    .page-nav a {
      color: #fff;
      text-decoration: none;
      margin: 0 10px;
    }
    .main-box {
      max-width: 600px;
      margin: 40px auto 100px;
      background: #fff;
      padding: 32px;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
    .btn {
      display: inline-block;
      margin-top: 24px;
      padding: 10px 20px;
      background-color: #007BFF;
      color: white;
      text-decoration: none;
      border: none;
      border-radius: 6px;
      cursor: pointer;
    }
    .error-message {
      color: red;
      margin-bottom: 16px;
      text-align: center;
    }
    .bottom-nav {
      position: fixed;
      bottom: 20px;
      width: 100%;
      text-align: center;
      background: #f9f9f9;
      padding: 12px 0;
      box-shadow: 0 -2px 6px rgba(0,0,0,0.1);
    }
    .form-control {
      width: 100%;
      padding: 8px;
      margin-bottom: 12px;
    }
    #cardFields {
      display: none;
    }
  </style>
</head>
<body>

<nav class="page-nav">
  <div class="nav-left">
    <a href="index.jsp">Home</a>
  </div>
  <div class="nav-right">
    <a href="edit.jsp">Edit</a>
    <a href="logout.jsp">Logout</a>
  </div>
</nav>

<div class="main-box">
  <h2>Checkout</h2>

  <% if (error != null) { %>
    <div class="error-message"><%= error %></div>
  <% } %>

  <form id="checkoutForm" action="saveOrders" method="post" onsubmit="return validateForm();">
    <p><strong>Total Amount:</strong> $<%= df.format(totalAmount) %></p>
    <input type="hidden" name="totalAmount" value="<%= totalAmount %>">

    <label for="deliveryAddress">Delivery Address:</label><br>
    <input type="text" name="deliveryAddress" id="deliveryAddress" class="form-control" required><br>

    <label for="paymentMethod">Payment Method:</label><br>
    <select id="paymentMethod" name="paymentMethod" class="form-control" required onchange="toggleCardFields(this.value)">
      <option value="">--Select Payment Method--</option>
      <option value="Card">Card</option>
      <option value="PayPal">PayPal</option>
    </select>

    <div id="cardFields">
      <label>Card Number:</label>
      <input type="text" id="cardNumber" name="cardNumber" class="form-control" placeholder="1234-5678-9012-3456">

      <label>CVV:</label>
      <input type="text" id="cvv" name="cvv" class="form-control" maxlength="3">

      <label>Name on Card:</label>
      <input type="text" name="cardHolderName" id="cardHolderName" class="form-control">

      <label>Expiry Date (MM/YY):</label>
      <input type="text" name="expiryDate" id="expiryDate" class="form-control" placeholder="MM/YY" maxlength="5">
    </div>

    <button type="submit" class="btn">Confirm Payment</button>
  </form>
</div>

<div class="bottom-nav">
  <a href="cart.jsp" class="btn">← Back to Cart</a>
</div>

<script>
  function toggleCardFields(value) {
    document.getElementById('cardFields').style.display = (value === 'Card') ? 'block' : 'none';
  }

  document.getElementById('cardNumber').addEventListener('input', function (e) {
    let input = e.target.value.replace(/[^\d]/g, '').substring(0, 16);
    input = input.match(/.{1,4}/g);
    e.target.value = input ? input.join('-') : '';
  });

  document.getElementById('cvv').addEventListener('input', function (e) {
    e.target.value = e.target.value.replace(/\D/g, '').substring(0, 3);
  });

  document.getElementById('expiryDate').addEventListener('input', function (e) {
    let val = e.target.value.replace(/[^\d]/g, '').substring(0, 4);
    if (val.length >= 3) val = val.slice(0, 2) + '/' + val.slice(2);
    e.target.value = val;
  });

  function validateForm() {
    const method = document.getElementById('paymentMethod').value;
    const delivery = document.getElementById('deliveryAddress').value.trim();

    if (!delivery) {
      alert("Please enter a delivery address.");
      return false;
    }

    if (method === 'Card') {
      const card = document.getElementById('cardNumber').value.trim();
      const cvv = document.getElementById('cvv').value.trim();
      const name = document.getElementById('cardHolderName').value.trim();
      const expiry = document.getElementById('expiryDate').value.trim();

      if (!card.match(/^\d{4}-\d{4}-\d{4}-\d{4}$/)) {
        alert("Card number must be in 1234-5678-9012-3456 format.");
        return false;
      }
      if (!cvv.match(/^\d{3}$/)) {
        alert("Please enter a valid 3-digit CVV.");
        return false;
      }
      if (!name) {
        alert("Please enter the cardholder's name.");
        return false;
      }
      if (!expiry.match(/^\d{2}\/\d{2}$/)) {
        alert("Expiry date must be in MM/YY format.");
        return false;
      }
      const [mm, yy] = expiry.split('/').map(Number);
      if (mm < 1 || mm > 12 || yy < 25) {
        alert("Expiry month must be 01-12 and year must be 25 or later.");
        return false;
      }
    }
    return true;
  }
</script>

</body>
</html>