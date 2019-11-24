package com.example.projectime.moodle;

public class Module {
    private Tab tab;

    private String id;
    private String name;
    private String url;
    private String type;

    public Module(Tab tab, String id, String name, String url, String type) {
        this.tab = tab;

        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
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
}
