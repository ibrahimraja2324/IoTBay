<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Device</title>
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


.edit-box {
  width: 420px;
  margin: 100px auto;
  background-color: #2c2a3a;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.3);
  color: #e0e0e0;
}

.edit-box h2 {
  text-align: center;
  color: #a6a6ff;
  margin-bottom: 20px;
  font-size: 24px;
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.edit-form label {
  color: #ccccff;
  font-weight: 500;
  font-size: 14px;
  margin-bottom: 4px;
  text-align: left;
}

.edit-form input[type="text"],
.edit-form input[type="number"] {
  background-color: #3a3760;
  color: #ffffff;
  padding: 12px;
  border: none;
  border-radius: 6px;
  font-size: 15px;
}

.edit-form input[type="submit"] {
  background: linear-gradient(to right, #28a745, #218838);
  color: #ffffff;
  padding: 12px 20px;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-form input[type="submit"]:hover {
  background: linear-gradient(to right, #218838, #28a745);
}

.btn-cancel {
  display: inline-block;
  background-color: #ccc;
  color: #000;
  text-align: center;
  border-radius: 6px;
  padding: 10px 0;
  text-decoration: none;
  margin-top: 10px;
  transition: background 0.3s;
}

.btn-cancel:hover {
  background-color: #bbb;
  color: #000;
}

.success-message {
  color: #5fd690;
  font-weight: 600;
  text-align: center;
  margin-bottom: 12px;
  display: none;
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
<div class="form-container">
    <div id="successMessage" class="success-message" style="display: none;">
        ✅ Device added successfully! Redirecting...
    </div>
    <div id="errorMessage" class="error-message"></div>
    <h2>Add New Device</h2>
    <form action="devices" method="post" onsubmit="return validateForm(event)">
        <input type="hidden" name="action" value="addDevice">

        <label for="name">Device Name:</label>
        <input type="text" name="name" id="name" required>

        <label for="type">Device Type:</label>
        <input type="text" name="type" id="type" required>

        <label for="unitPrice">Unit Price:</label>
        <input type="number" step="0.01" name="unitPrice" id="unitPrice" required>

        <label for="quantity">Quantity:</label>
        <input type="number" name="quantity" id="quantity" required>

        <button type="submit" class="submit-button">Add Device</button>
        <a href="devices?action=list" class="cancel-button">Cancel</a>
    </form>
</div>
</body>
</html>
