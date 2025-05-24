<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="iotbay.model.Device" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Device</title>
    <style>
body {
  background-color: #1f1c2c;
  color: #ffffff;
  font-family: 'Quicksand', sans-serif;
  padding-top: 80px;
}

.form-container {
  width: 440px;
  margin: 100px auto;
  background-color: #2c2a3a;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  color: #e0e0e0;
  text-align: center;
}

.form-container h2 {
  color: #a6a6ff;
  margin-bottom: 20px;
  font-size: 24px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

label {
  color: #ccccff;
  font-weight: 500;
  font-size: 14px;
  margin-bottom: 4px;
  text-align: left;
}

input[type="text"],
input[type="number"] {
  background-color: #3a3760;
  color: #ffffff;
  padding: 12px;
  border: none;
  border-radius: 6px;
  font-size: 15px;
}

.submit-button,
.cancel-button {
  padding: 12px 20px;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-button {
  background: linear-gradient(to right, #2575fc, #6a11cb);
  color: #000;
  text-decoration: none;
  text-align: center;
}

.submit-button:hover {
  background: linear-gradient(to right, #6a11cb, #2575fc);
}

.cancel-button {
  background-color: #f1c40f;
  color: #000;
  text-decoration: none;
  text-align: center;
}

.cancel-button:hover {
  background-color: #d4ac0d;
  color: #000;
}

.success-message {
  color: #5fd690;
  font-weight: 600;
  text-align: center;
  margin-bottom: 12px;
  display: none;
}

.error-message {
  color: #ff6b6b;
  font-weight: 600;
  text-align: center;
  margin-bottom: 12px;
  display: none;
}
    </style>
</head>
<body>
<script>
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get("success") === "true") {
        document.addEventListener("DOMContentLoaded", () => {
            const msg = document.getElementById("successMessage");
            msg.style.display = "block";
            setTimeout(() => {
                window.location.href = "devices?action=list";
            }, 2000);
        });
    }
    if (urlParams.get("error") === "true") {
        document.addEventListener("DOMContentLoaded", () => {
            const errorMsg = document.getElementById("updateError");
            errorMsg.style.display = "block";
        });
    }

    function validateForm(event) {
        const name = document.getElementById("name").value.trim();
        const type = document.getElementById("type").value.trim();
        const price = parseFloat(document.getElementById("unitPrice").value);
        const quantity = parseInt(document.getElementById("quantity").value);
        const errorMsg = document.getElementById("errorMessage");

        if (name === "" || type === "") {
            errorMsg.textContent = "Name and Type must not be empty.";
            errorMsg.style.display = "block";
            event.preventDefault();
            return false;
        }
        if (isNaN(price) || price <= 0) {
            errorMsg.textContent = "Price must be a positive number.";
            errorMsg.style.display = "block";
            event.preventDefault();
            return false;
        }
        if (isNaN(quantity) || quantity < 0) {
            errorMsg.textContent = "Quantity must be zero or a positive integer.";
            errorMsg.style.display = "block";
            event.preventDefault();
            return false;
        }
        errorMsg.style.display = "none";
        return true;
    }
</script>
<%
    Device device = (Device) request.getAttribute("device");
    if (device == null) {
%>
    <p>Device not found.</p>
<%
    } else {
%>
<div class="form-container">
    <div id="successMessage" class="success-message">✅ Device updated successfully! Redirecting...</div>
    <div id="updateError" class="error-message">❌ Failed to update device. Please try again.</div>
    <div id="errorMessage" class="error-message"></div>
    <h2>Edit Device</h2>
    <form action="devices" method="post" onsubmit="return validateForm(event)">
        <input type="hidden" name="action" value="updateDevice">
        <input type="hidden" name="id" value="<%= device.getId() %>">

        <label for="name">Device Name:</label>
        <input type="text" name="name" id="name" value="<%= device.getName() %>" required>

        <label for="type">Device Type:</label>
        <input type="text" name="type" id="type" value="<%= device.getType() %>" required>

        <label for="unitPrice">Unit Price:</label>
        <input type="number" step="0.01" name="unitPrice" id="unitPrice" value="<%= device.getUnitPrice() %>" required>

        <label for="quantity">Quantity:</label>
        <input type="number" name="quantity" id="quantity" value="<%= device.getQuantity() %>" required>

        <button type="submit" class="submit-button">Update Device</button>
        <a href="devices?action=list" class="cancel-button">Cancel</a>
    </form>
</div>
<%
    }
%>
</body>
</html>
