package edu.stthomas.repo;

import edu.stthomas.model.ReturnTransaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReturnsRepo {
    //this would be in file or database
    public static final Map<Integer, ReturnTransaction> returns = new HashMap<>();
    private static int returnId = 0;
    private int id;

//    public int save(ReturnTransaction returnRecord) {
    public String save(ReturnTransaction returnRecord) {
//        id = ++returnId;
        String returnId =  UUID.randomUUID().toString().substring(0,8);
        returns.put(id, returnRecord);
        returnRecord.setId(returnId);
        return returnId;
    }

    public static Map<Integer, ReturnTransaction> getAllReturns() {
        return returns;
    }

//    public ReturnTransaction getReturnsRecord(int returnId) {
    public ReturnTransaction getReturnsRecord(String returnId) {
        return returns.get(returnId);
    }
}
