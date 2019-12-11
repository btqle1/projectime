package com.example.projectime.moodle;

public class Module {
    private Tab tab;

    private String id;
    private String name;
    private String url;
    private String type;
    private long time;

    public Module(Tab tab, String id, String name, String url, String type, long time) {
        this.tab = tab;

        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
    }
}
