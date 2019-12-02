package com.example.projectime;

import java.util.ArrayList;

public class Calendar {
    private String name;
    private ArrayList<Tab> tabs;

    public Calendar(String name, ArrayList<Tab> tabs) {
        this.name = name;
        this.tabs = tabs;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Tab> getTabs() {
        return tabs;
    }
}
