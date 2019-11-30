package edu.stthomas.model;

import edu.stthomas.helper.Helper;

public class AbstractLineItem implements LineItem {
    protected int itemId;
    protected int quantity;
    protected double price;
    protected double tax;

    public AbstractLineItem(int quantity) {
        this.quantity = quantity;
    }

    public AbstractLineItem(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return Helper.roundUp(price);
    }

    public double getTax() {
        return tax;
    }

    public double getLineItemAntBeforeTax() {
        return Helper.roundUp(getQuantity() * getPrice());
    }

    public double getLineItemTax() {
        return Helper.roundUp(getLineItemAntBeforeTax() * getTax());
    }

    /**
     * total of sale amt + tax
     * @return
     */
    public double getLineItemAmt() {
        return Helper.roundUp(getLineItemAntBeforeTax() + getLineItemTax());
    }
}
