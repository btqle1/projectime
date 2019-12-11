package com.example.projectime;

public class Event {
    private String name;
    private String url;
    private long time;

    public Event(String name, String url, long time) {
        this.name = name;
        this.url = url;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public long getTime() {
        return time;
    }
}
