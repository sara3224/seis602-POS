package edu.stthomas.model;

import edu.stthomas.helper.Helper;
import edu.stthomas.service.PricingService;
import edu.stthomas.service.TaxService;

public class ReturnLineItem {
    private int itemId;
    private int quantity;
    private double price;
    private double tax;
    private String reason; //why item was returned
    private static TaxService taxService = new TaxService();
    private static PricingService pricingService = new PricingService();

    public ReturnLineItem(SalesLineItem salesLineItem, int qty, String reason) {
        this.itemId = salesLineItem.getItemId();
        this.quantity = qty;
        this.price = salesLineItem.getPrice();
        this.tax = salesLineItem.getTax();
        this.reason = reason;
    }

//    public ReturnLineItem(int itemId, int quantity) {
//        this.itemId = itemId;
//        this.quantity = quantity;
//        this.price = pricingService.getPrice(itemId);
//        tax = taxService.getTax(itemId);
//    }

    public int getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }


    public double getPrice() {
        return price;
    }

    public double getTax() {
        return tax;
    }

    public double getLineItemRefund() {
        return Helper.roundUp(getQuantity() * getPrice());
    }

    public double getLineItemTax() {
        return Helper.roundUp(getLineItemRefund() * getTax());
    }

    /**
     * total of sale amt + tax
     * @return
     */
    public double getLineItemAmt() {
        return Helper.roundUp(getLineItemRefund() + getLineItemTax());
    }
}
