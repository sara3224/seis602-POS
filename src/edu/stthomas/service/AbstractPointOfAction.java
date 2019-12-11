package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.Item;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.InventoryRepo;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPointOfAction implements IPointOfAction {
    protected String cashierId;
    protected Shift shift;
    protected String registerId;
    protected Map<String, Integer> itemsAndQuantity = new HashMap<>();
    protected String saleId;
    protected SalesTransaction salesRecord;


    public AbstractPointOfAction(String cashierId, Shift shift, String registerId) {
        this.cashierId = cashierId;
        this.shift = shift;
        this.registerId = registerId;
    }

    public String getSaleId() {
        return saleId;
    }

    /**
     * Overrides the quantity for item.
     * @param item_id
     * @param qty
     */
    @Override
    public String addItem(String item_id, int qty) {
        Item item = InventoryRepo.getItem(item_id);
        if(item == null) {
            return "Item: " +item_id+ " does not exist...Please enter valid item";
        } else if(qty > item.getOnhands()) {
            return "For item" +item.getItemId() +"i.e. "+ item.getName()+" max quantity available is: "+item.getOnhands();
        }
        itemsAndQuantity.put(item_id, qty);
        return "";
    }

    @Override
    public Map<String, Integer> getItemsAndQuantity() {
        return itemsAndQuantity;
    }

    @Override
    public void removeItem(String item_id) {
        getItemsAndQuantity().remove(item_id);
    }

    @Override
    public String toString() {
        recordPrint();
        return "";
    }

    abstract void recordPrint();

}
