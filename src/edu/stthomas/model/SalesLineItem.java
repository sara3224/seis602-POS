package edu.stthomas.model;

import edu.stthomas.helper.Helper;
import edu.stthomas.service.PricingService;
import edu.stthomas.service.TaxService;

public class SalesLineItem {
    private int itemId;
    private int quantity;
    private double price;
    private double tax;
    private static TaxService taxService = new TaxService();
    private static PricingService pricingService = new PricingService();

    public SalesLineItem(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.price = pricingService.getPrice(itemId);
        tax = taxService.getTax(itemId);
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
