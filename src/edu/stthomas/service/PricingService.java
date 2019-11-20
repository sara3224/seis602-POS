package edu.stthomas.service;

import java.util.HashMap;
import java.util.Map;

public class PricingService {
    static Map<Integer, Double> pricing = new HashMap<>();

    static{
        pricing.put(1, 2.2);
        pricing.put(2, 120.0);
        pricing.put(3, 500.0);
    }

    public void updateOraddPrice(Integer item_id, Double newPrice) {
        pricing.put(item_id, newPrice);
    }

    public void removeItem(Integer item_id) {
        pricing.remove(item_id);
    }

    public double getPrice(Integer item_id) {
        return pricing.get(item_id);
    }

}
