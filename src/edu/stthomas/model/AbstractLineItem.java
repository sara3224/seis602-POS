package edu.stthomas.model;

import edu.stthomas.helper.Helper;

public class AbstractLineItem implements LineItem {
    protected String itemId;
    protected int quantity;
    protected double price;
    protected double tax;

    public AbstractLineItem() {}

    public AbstractLineItem(int quantity) {
        this.quantity = quantity;
    }

    public AbstractLineItem(String itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() {
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

    public double getLineItemAmtBeforeTax() {
        return Helper.roundUp(getQuantity() * getPrice());
    }

    public double getLineItemTax() {
        return Helper.roundUp(getLineItemAmtBeforeTax() * getTax());
    }

    /**
     * total of sale amt + tax
     * @return
     */
    public double getLineItemAmt() {
        return Helper.roundUp(getLineItemAmtBeforeTax() + getLineItemTax());
    }
}
