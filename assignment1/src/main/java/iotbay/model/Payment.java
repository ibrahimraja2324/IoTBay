package iotbay.model;

public class Payment {
    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private String cardDetails;
    private double amount;
    private String date;
    private String userEmail;


    public Payment(int paymentId, int orderId, String paymentMethod, String cardDetails, double amount, String date, String userEmail) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.cardDetails = cardDetails;
        this.amount = amount;
        this.date = date;
        this.userEmail = userEmail;
    }
    
    
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getCardDetails() {
        return cardDetails;
    }
    
    public void setCardDetails(String cardDetails) {
        this.cardDetails = cardDetails;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
