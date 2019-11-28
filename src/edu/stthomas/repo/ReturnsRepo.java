package edu.stthomas.repo;

import edu.stthomas.model.ReturnRecord;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ReturnsRepo {
    //this would be in file or database
    public static final Map<Integer, ReturnRecord> returns = new HashMap<>();
    private static int returnId = 0;
    private int id;

    public int save(ReturnRecord returnRecord) {
        id = ++returnId;
        returns.put(id, returnRecord);
        return id;
    }

    public static Map<Integer, ReturnRecord> getAllReturns() {
        return returns;
    }

    public ReturnRecord getReturnsRecord(int returnId) {
        return returns.get(returnId);
    }
}
