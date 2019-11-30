package edu.stthomas.model;

public class Inventory {
    private int itemId;
    private int qty;
    private double price;
    private int threshold;

    public Inventory(int itemId, int qty, double price, int threshold) {
        this.itemId = itemId;
        this.price = price;
        this.qty = qty;
        this.threshold = threshold;
    }

    public double getPrice() {
        return price;
    }
}
