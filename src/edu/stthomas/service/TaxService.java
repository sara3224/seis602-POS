package edu.stthomas.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains percentage of tax for each item
 */
public class TaxService {

    static Map<Integer, Double> tax = new HashMap<>();

    static{
        tax.put(1, 0.075);
        tax.put(2, 0.000);
        tax.put(3, 0.075);
    }

    public void updateOraddTax(Integer item_id, Double newPrice) {
        tax.put(item_id, newPrice);
    }

    public void removeItem(Integer item_id) {
        tax.remove(item_id);
    }

    public double getTax(Integer item_id) {
        return tax.get(item_id);
    }
}
