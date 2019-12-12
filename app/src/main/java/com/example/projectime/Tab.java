package com.example.projectime;

import java.util.ArrayList;

public class Tab {
    private String name;
    private ArrayList<Event> events;

    public Tab(String name, ArrayList<Event> events) {
        this.name = name;
        this.events = events;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public String getName() {
        return name;
    }
}
