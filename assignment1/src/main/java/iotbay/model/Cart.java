package iotbay.model;

import java.util.ArrayList;
import java.util.List;

// Represents a shopping cart containing a list of cart items
public class Cart {
    private List<CartItem> items = new ArrayList<>();  // List to store cart items

    // Adds an item to the cart and logs the addition
    public void addItem(CartItem item) {
        items.add(item);
        System.out.println("Added item: " + item.getDevice().getName() + " | Total items: " + items.size());
    }

    // Removes a specific item from the cart
    public void removeItem(CartItem item) {
        items.remove(item);
    }

    // Returns a list of all cart items
    public List<CartItem> getItems() {
        return items;
    }

    // Sets the list of cart items (replaces existing list)
    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    // Removes an item from the cart based on device ID
    public void removeItem(int deviceId) {
        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getDevice().getId() == deviceId) {
                itemToRemove = item;
                break;  // Stop searching once the item is found
            }
        }
        if (itemToRemove != null) {
            items.remove(itemToRemove);
        }
    }

    // Clears all items from the cart and logs the action
    public void clearItems() {
        items.clear();
        System.out.println("Cleared all items from the cart.");
    }

    // Calculates the total price of all items in the cart
    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getDevice().getUnitPrice() * item.getQuantity();
        }
        return total;
    }

    // Calculates the total quantity of all items in the cart
    public int getTotalQuantity() {
        int quantity = 0;
        for (CartItem item : items) {
            quantity += item.getQuantity();
        }
        return quantity;
    }
}