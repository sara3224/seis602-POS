package edu.stthomas.repo;

import edu.stthomas.model.ReturnTransaction;

import java.util.HashMap;
import java.util.Map;

public class ReturnsRepo {
    //this would be in file or database
    public static final Map<Integer, ReturnTransaction> returns = new HashMap<>();
    private static int returnId = 0;
    private int id;

    public int save(ReturnTransaction returnRecord) {
        id = ++returnId;
        returns.put(id, returnRecord);
        return id;
    }

    public static Map<Integer, ReturnTransaction> getAllReturns() {
        return returns;
    }

    public ReturnTransaction getReturnsRecord(int returnId) {
        return returns.get(returnId);
    }
}
