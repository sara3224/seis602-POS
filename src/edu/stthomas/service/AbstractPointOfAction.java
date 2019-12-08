package edu.stthomas.service;

import edu.stthomas.enums.Shift;
import edu.stthomas.model.Item;
import edu.stthomas.model.SalesTransaction;
import edu.stthomas.repo.InventoryRepo;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPointOfAction implements IPointOfAction {
//    protected int cashierId;
    protected String cashierId;
    protected Shift shift;
    protected int registerId;
    protected Map<Integer, Integer> itemsAndQuantity = new HashMap<>();
//    protected int saleId;
    protected String saleId;
    protected SalesTransaction salesRecord;

//    public AbstractPointOfAction(int cashierId, Shift shift, int registerId) {
//        this.cashierId = cashierId;
//        this.shift = shift;
//        this.registerId = registerId;
//    }

    public AbstractPointOfAction(String cashierId, Shift shift, int registerId) {
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
    public String addItem(Integer item_id, int qty) {
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
