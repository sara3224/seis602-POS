package edu.stthomas.model;

import java.util.ArrayList;
import java.util.List;

public class Register {

    private String registerId;
    public static List<String> registers = new ArrayList<>();

    static {
        registers.add("1");
        registers.add("2");
        registers.add("3");
        registers.add("4");
        registers.add("4");
        registers.add("5");
        registers.add("6");
        registers.add("7");
        registers.add("8");
        registers.add("9");
        registers.add("10");
    }

    public Register(String id) {
        registerId = id;
    }

    public String getRegisterId() {
        return registerId;
    }

}
