<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Payment" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Payment Method</title>
</head>
<body>
    <h1>Edit Payment Method</h1>
    <%
        Payment paymentMethod = (Payment) request.getAttribute("paymentMethod");
        if (paymentMethod == null) {
    %>
        <p>Payment method not found.</p>
    <%
        } else {
    %>
    <form action="PaymentMethodServlet?action=update" method="post">

        <input type="hidden" name="paymentId" value="<%= paymentMethod.getPaymentId() %>">
        <label for="paymentMethod">Payment Method:</label>
        <input type="text" id="paymentMethod" name="paymentMethod" value="<%= paymentMethod.getPaymentMethod() %>" required><br><br>
        
        <label for="cardDetails">Card Details:</label>
        <input type="text" id="cardDetails" name="cardDetails" value="<%= paymentMethod.getCardDetails() %>" required><br><br>
        
        <label for="expiryDate">Expiry Date:</label>
        <input type="date" id="expiryDate" name="expiryDate" value="<%= paymentMethod.getDate() %>" required><br><br>
        
        <input type="submit" value="Update Payment Method">
    </form>
    <%
        }
    %>
</body>
</html>
