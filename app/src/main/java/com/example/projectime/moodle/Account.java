package com.example.projectime.moodle;

public class Account {
    private String instanceUrl;
    private String username;
    private String password;

    private String authToken;

    public Account(String instanceUrl, String authToken, String username, String password) {
        this.instanceUrl = instanceUrl;
        this.username = username;
        this.password = password;
    }
}
