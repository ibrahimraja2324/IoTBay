package iotbay.model;

public class Shipment {
    private int shipmentId;
    private int orderId;
    private String shipmentMethod;
    private String shipmentDate;
    private String address;
    private String status;
    private String userEmail;

    public Shipment(int shipmentId, int orderId, String shipmentMethod, String shipmentDate, 
                   String address, String status, String userEmail) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.shipmentMethod = shipmentMethod;
        this.shipmentDate = shipmentDate;
        this.address = address;
        this.status = status;
        this.userEmail = userEmail;
    }

    // Default constructor for creating a new shipment
    public Shipment() {
    }

    // Getters and Setters
    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getShipmentMethod() {
        return shipmentMethod;
    }

    public void setShipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}