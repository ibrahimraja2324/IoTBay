package iotbay.model;

public class Order {
    private int orderId;
    private String orderDate;
    private String status;
    private double totalAmount;
    private String userEmail;

    private int userId;
    private int productId;
    private int quantity;
    private String deliveryAddress;
    private double totalPrice;

    private String paymentMethod; 

    public Order() { }

    public Order(int orderId, String status, double totalAmount, String userEmail) {
        this.orderId = orderId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.userEmail = userEmail;
    }

    public Order(int orderId, String orderDate, String status, double totalAmount, String userEmail,
                 int userId, int productId, int quantity, String deliveryAddress, double totalPrice, String paymentMethod) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.userEmail = userEmail;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    // Getter & Setter
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}