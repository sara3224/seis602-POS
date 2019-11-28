package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.SalesRecord;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPointOfAction implements IPointOfAction {
    protected int cashierId;
    protected Shift shift;
    protected int registerId;
    protected Map<Integer, Integer> itemsAndQuantity = new HashMap<>();
    protected int saleId;
    protected SalesRecord salesRecord;

    public AbstractPointOfAction(int cashierId, Shift shift, int registerId) {
        this.cashierId = cashierId;
        this.shift = shift;
        this.registerId = registerId;
    }

    /**
     * Overrides the quantity for item.
     * @param item_id
     * @param qty
     */
    @Override
    public void addItem(Integer item_id, int qty) {
        itemsAndQuantity.put(item_id, qty);
    }

    @Override
    public Map<Integer, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    @Override
    public void removeItem(Integer item_id) {
        getItemsAndQuantity().remove(item_id);
    }

    @Override
    public String toString() {
        recordPrint();
        return "";
    }

    abstract void recordPrint();

}
