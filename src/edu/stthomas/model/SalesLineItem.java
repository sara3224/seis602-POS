package edu.stthomas.model;

import edu.stthomas.repo.InventoryRepo;

public class SalesLineItem extends AbstractLineItem {
    public SalesLineItem(int itemId, int quantity) {
        super(itemId, quantity);
        Item item = InventoryRepo.getItem(itemId);
        price = item.getPrice();
        tax = item.getTax();
    }
}
