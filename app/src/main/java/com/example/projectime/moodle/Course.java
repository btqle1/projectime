package com.example.projectime.moodle;

public class Course {
    private Account account;

    private int id;
    private String url;
    private String name;

    public Course(Account account, int id, String url, String name) {
        this.account = account;
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
