package edu.stthomas.model;

import java.util.HashMap;
import java.util.Map;

public class Cashier {
//    private int id;
    private String id;
//    private static Map<Integer, Integer> cashierLevels = new HashMap<>();
    private static Map<String, Integer> cashierLevels = new HashMap<>();

    static {
        cashierLevels.put("sara3224",1);
//        cashierLevels.put(2,2);
//        cashierLevels.put(1001,2);
//        cashierLevels.put(1002,1);
    }

//    public Cashier(int id) {
    public Cashier(String id) {
        this.id = id;
    }

//    public int getId() {
    public String getId() {
        return id;
    }

    public int getLevel() {
        return cashierLevels.get(getId());
    }
}
