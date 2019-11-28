package edu.stthomas.repo;

import edu.stthomas.model.SalesRecord;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SalesRepo {
    //this would be in file or database
    public static final Map<Integer,SalesRecord> sales = new HashMap<>();
    private static int salesId = 0;
    private int id;

    public int save(SalesRecord salesRecord) {
        id = ++salesId;
        sales.put(id, salesRecord);
        return id;
    }

    public static Collection<SalesRecord> getSales() {
        return sales.values();
    }

    public static SalesRecord getSalesRecord(int salesId) {
        return sales.get(salesId);
    }
}
