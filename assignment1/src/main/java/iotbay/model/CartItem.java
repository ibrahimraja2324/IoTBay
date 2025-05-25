package iotbay.model;

public class CartItem {
    private Device device;
    private int quantity;

    public CartItem() {}

    public CartItem(Device device, int quantity) {
        this.device = device;
        this.quantity = quantity;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}