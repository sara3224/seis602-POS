package edu.stthomas.service;

import java.util.Map;

/**
 * Any action that could be performed by store cachier like POS or return
 */
public interface IPointOfAction {
    String addItem(String item_id, int qty);

    Map<String, Integer> getItemsAndQuantity();

    void removeItem(String item_id);
}
