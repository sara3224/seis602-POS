package edu.stthomas.model;

import edu.stthomas.service.PricingService;
import edu.stthomas.service.TaxService;

public class SalesLineItem extends AbstractLineItem {
    private static TaxService taxService = new TaxService();
    private static PricingService pricingService = new PricingService();

    public SalesLineItem(int itemId, int quantity) {
        super(itemId, quantity);
        //TODO: get price from the inventory.txt
        this.price = pricingService.getPrice(itemId);
        tax = taxService.getTax(itemId);
    }
}
