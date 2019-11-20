package edu.stthomas.model;

import java.util.HashMap;
import java.util.Map;

public class Cashier {
    private int id;
    private int level;
    private static Map<Integer, Integer> cashierLevels = new HashMap<>();

    static {
        cashierLevels.put(1,1);
        cashierLevels.put(2,2);
        cashierLevels.put(1001,2);
        cashierLevels.put(1002,1);
    }

    public Cashier(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return cashierLevels.get(getId());
    }
}
