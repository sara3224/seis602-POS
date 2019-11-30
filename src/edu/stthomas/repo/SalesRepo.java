package edu.stthomas.repo;

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
        return salesId;
    }

    public static Collection<SalesTransaction> getSales() {
        return sales.values();
    }

    public static SalesTransaction getSalesRecord(int salesId) {
        return sales.get(salesId);
    }
}
