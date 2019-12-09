package edu.stthomas.model;

public class ReturnLineItem extends AbstractLineItem {
    private String reason; //why item was returned

    public ReturnLineItem(SalesLineItem salesLineItem, int qty, String reason) {
        super(qty);
        this.itemId = salesLineItem.getItemId();
        this.price = salesLineItem.getPrice();
        this.tax = salesLineItem.getTax();
        this.reason = reason;
    }

//    public ReturnLineItem(SalesLineItem salesLineItem, int qty, String reason) {
//        super(qty);
//        this.itemId = salesLineItem.getItemId();
//        this.price = salesLineItem.getPrice();
//        this.tax = salesLineItem.getTax();
//        this.reason = reason;
//    }

    public String getReason() {
        return reason;
    }
}
