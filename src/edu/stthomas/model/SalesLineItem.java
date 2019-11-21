package edu.stthomas.model;

public class SalesLineItem {
    private int itemId;
    private int quantity;
    private double amt;

    public SalesLineItem(int itemId, int quantity, double amt) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.amt = amt;

    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmt() {
        return amt;
    }

    public double getLineItemAmt() {
        return getQuantity() * getAmt();
    }
}
