package com.example.projectime;

public class Event {
    private String name;
    private String url;

    public Event(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
