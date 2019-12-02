package edu.stthomas.service;

import edu.stthomas.repo.InventoryRepo;

public class InventoryService {

    private int itemId;
    private int qty;
    private double price;
    private int threshold;

    public void registerItem(int itemId, int qty, int price, double tax, int threshold) {
        InventoryRepo.addItem(itemId, qty, price,tax, threshold);
    }
}
