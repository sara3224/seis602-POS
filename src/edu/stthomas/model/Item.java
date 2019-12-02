package edu.stthomas.model;

/**
 * read inventory details from the file.
 */
public class Item {
    private int itemId;
    private int qty;
    private double price;
    private double tax;
    private int threshold;
    private int outstanding;

    public Item(int itemId, int qty, double price, double tax, int threshold, int outstanding) {
        this.itemId = itemId;
        this.price = price;
        this.tax = tax;
        this.qty = qty;
        this.threshold = threshold;
        this.outstanding = outstanding;
    }

    public int getItemId() {
        return itemId;
    }

    public int getThreshold() {
        return threshold;
    }

    public double getPrice() {
        return price;
    }

    public double getTax() {
        return tax;
    }

    public int getQty(){
        return qty;
    }

    public int getOutstanding() {
        return outstanding;
    }
}
