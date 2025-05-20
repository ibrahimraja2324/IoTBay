<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Payment Dashboard - IoTBay</title>
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
    /* New Payment Method Toggle */
    .toggle-link {
      display: block;
      text-align: center;
      color: #2575fc;
      text-decoration: underline;
      cursor: pointer;
      margin-bottom: 15px;
    }
    /* Hidden form for new payment method */
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
    .new-payment-form input[type="date"] {
      width: 100%;
      padding: 8px;
      margin-bottom: 10px;
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
    /* Table Styling */
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
  <!-- NavBar -->
  <nav class="page-nav">
    <div class="nav-left">

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
          <label for="paymentMethod">Payment Method:</label>
          <input type="text" id="paymentMethod" name="paymentMethod" placeholder="e.g., Visa, MasterCard" required>
          
          <label for="cardDetails">Card Details:</label>
          <input type="text" id="cardDetails" name="cardDetails" placeholder="Card Number" required>
          
          <label for="expiryDate">Expiry Date:</label>
          <input type="date" id="expiryDate" name="expiryDate" required>
          
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
