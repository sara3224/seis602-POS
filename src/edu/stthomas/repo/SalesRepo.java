package edu.stthomas.repo;

import edu.stthomas.model.Item;
import edu.stthomas.model.SalesLineItem;
import edu.stthomas.model.SalesTransaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SalesRepo {
    //this would be in file or database
    //TODO: consider using queue?
    public static final Map<Integer, SalesTransaction> sales = new HashMap<>();

    private static int salesId = 0;

    public int save(SalesTransaction salesRecord) {
        sales.put(++salesId, salesRecord);
        //TODO sold qty from deduct from inventory.txt..look InventoryRepo
        updateInventory(salesRecord);
        return salesId;
    }

    /**
     * Only valid transaction update inventory
     * @param salesRecord
     */
    private void updateInventory(SalesTransaction salesRecord) {
        for (SalesLineItem salesLineItem: salesRecord.getSalesLineItems()) {
            Item item = InventoryRepo.getItem(salesLineItem.getItemId());
            int newOH =  (item.getQty() - salesLineItem.getQuantity());
            int outStanding = item.getOutstanding();
            if(newOH < 0) {
                outStanding = item.getOutstanding() + Math.abs(newOH);
            }
            int updatedQty = newOH>=0 ? newOH:0;
            Item updatedItem = new Item(item.getItemId(),updatedQty, item.getPrice(), item.getTax(),
                    item.getThreshold(), outStanding);

            InventoryRepo.updateItem(updatedItem);
        }
    }

    public static Collection<SalesTransaction> getSales() {
        return sales.values();
    }

    public static SalesTransaction getSalesRecord(int salesId) {
        return sales.get(salesId);
    }
}
