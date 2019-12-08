package edu.stthomas.service;

import java.util.Map;

public interface IPointOfAction {
    String addItem(Integer item_id, int qty);

    Map<Integer, Integer> getItemsAndQuantity();

    void removeItem(Integer item_id);
}
