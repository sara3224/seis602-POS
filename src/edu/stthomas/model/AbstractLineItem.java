package edu.stthomas.model;

import edu.stthomas.helper.Helper;

public class AbstractLineItem {
    protected int itemId;
    protected int quantity;
    protected double price;
    protected double tax;

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

    public double getLineItemSale() {
        return Helper.roundUp(getQuantity() * getPrice());
    }

    public double getLineItemTax() {
        return Helper.roundUp(getLineItemSale() * getTax());
    }

    /**
     * total of sale amt + tax
     * @return
     */
    public double getLineItemAmt() {
        return Helper.roundUp(getLineItemSale() + getLineItemTax());
    }
}
