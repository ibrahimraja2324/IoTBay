package iotbay.model;

public class Device {
    private int id;            
    private String name;          
    private String type;     
    private double unitPrice;    
    private int quantity;     

    public Device(int id, String name, String type, double unitPrice, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Device{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", unitPrice=" + unitPrice +
               ", quantity=" + quantity +
               '}';
    }
}