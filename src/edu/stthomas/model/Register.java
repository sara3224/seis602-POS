package edu.stthomas.model;

import java.util.ArrayList;
import java.util.List;

public class Register {

    private Integer registerId;
    private static List<Integer> registers = new ArrayList<>();

    static {
        registers.add(1);
        registers.add(2);
        registers.add(3);
        registers.add(4);
    }

    public Register(int id) {
        registerId = id;
    }

    public int getRegisterId() {
        return registerId;
    }

}
