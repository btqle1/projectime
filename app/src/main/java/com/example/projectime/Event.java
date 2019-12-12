package com.example.projectime;

public class Event {
    private String name;
    private String url;
    private boolean isTimeKnown;
    private long time;

    public Event(String name, String url, boolean timeIsKnown, long time) {
        this.name = name;
        this.url = url;
        this.isTimeKnown = timeIsKnown;
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

    public boolean isTimeKnown() {
        return isTimeKnown;
    }
}
