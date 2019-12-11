package edu.stthomas.model;

/**
 * read inventory details from the file.
 */
public class Item {
    private String itemId;
    private String name;
    private int onhands;
    private double price;
    private double tax;
    private int threshold;
    private String supplierId;
    private int reorderQty;
    private int pending;

    public Item(String itemId, String name, int onhands, double price, double tax, int threshold, String supplierId, int reorderQty, int pending) {
        this.itemId = itemId;
        this.name = name;
        this.onhands = onhands;
        this.price = price;
        this.tax = tax;
        this.threshold = threshold;
        this.supplierId = supplierId;
        this.reorderQty = reorderQty;
        this.pending = pending;
    }

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public int getOnhands(){
        return onhands;
    }

    public double getPrice() {
        return price;
    }

    public double getTax() {
        return tax;
    }

    public int getThreshold() {
        return threshold;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public int getReorderQty() {
        return reorderQty;
    }

    public int getPending() {
        return pending;
    }
}
