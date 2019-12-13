package edu.stthomas.service;

import java.util.Map;

public interface IPointOfAction {
    String addItem(String item_id, int qty);

    Map<String, Integer> getItemsAndQuantity();

    void removeItem(String item_id);
}
