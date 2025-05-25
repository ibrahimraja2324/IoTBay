package iotbay.model;

import java.util.ArrayList;
import java.util.List;

// Represents a shopping cart containing a list of cart items
public class Cart {
    private List<CartItem> items;

    // Default constructor initializes an empty list
    public Cart() {
        this.items = new ArrayList<>();
    }

    // Constructor with item list
    public Cart(List<CartItem> items) {
        this.items = new ArrayList<>(items);
    }

    // Adds an item to the cart (e.g. new device added)
    public void addItem(CartItem item) {
        items.add(item);
        System.out.println("Added item: " + item.getDevice().getName() + " | Total items: " + items.size());
    }

    // Removes a specific item (object-based)
    public void removeItem(CartItem item) {
        items.remove(item);
    }

    // Removes an item by device ID
    public void removeItem(int deviceId) {
        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getDevice().getId() == deviceId) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
        }
    }

    // Clears the entire cart
    public void clearItems() {
        items.clear();
        System.out.println("Cleared all items from the cart.");
    }

    // Returns all cart items
    public List<CartItem> getItems() {
        return items;
    }

    // Replaces current items with a new list
    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    // Returns total price of all items
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getDevice().getUnitPrice() * item.getQuantity();
        }
        return total;
    }

    // Returns total quantity of all items
    public int getTotalQuantity() {
        int quantity = 0;
        for (CartItem item : items) {
            quantity += item.getQuantity();
        }
        return quantity;
    }
}