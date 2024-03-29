package com.example.projectime.moodle;

import java.io.IOException;
import java.util.ArrayList;

public class DataSource {
    private Account account;

    public DataSource(String username, String password, String instanceUrl) throws IOException {
        this.account = new Account(instanceUrl, username, password);
    }

    public ArrayList<String> getCalendars() {
        ArrayList<String> calendars = new ArrayList<>();
        ArrayList<Course> courses;
        try {
            courses = account.getCourses();
        } catch (IOException e) {
            return new ArrayList<String>();
        }
        for(Course course : courses) {
            calendars.add(course.getName());
        }
        return calendars;
    }

    public ArrayList<String> getTabs(int calendarIndex) {
        ArrayList<String> calendarTabs = new ArrayList<>();
        ArrayList<Tab> tabs;
        try {
            tabs = account.getCourses().get(calendarIndex).getTabs();
        } catch (IOException e) {
            return new ArrayList<String>();
        }
        for (Tab tab : tabs) {
            calendarTabs.add(tab.getName());
        }
        return calendarTabs;
    }

    public ArrayList<String> getEvents(int calendarIndex, int tabIndex) throws IOException {
        ArrayList<String> events = new ArrayList<>();
        ArrayList<Module> modules;
        try {
            modules = account.getCourses()
                    .get(calendarIndex)
                    .getTabs()
                    .get(tabIndex)
                    .getModules();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        for(Module module : modules) {
            events.add(module.getName());
        }

        return events;
    }

    public ArrayList<Long> getTimes(int calendarIndex, int tabIndex) {
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Module> modules;
        try {
            modules = account.getCourses()
                    .get(calendarIndex)
                    .getTabs()
                    .get(tabIndex)
                    .getModules();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        for(Module module : modules) {
            times.add(module.getTime());
        }

        return times;
    }

    public ArrayList<String> getURLs(int calendarIndex, int tabIndex) throws IOException {
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<Module> modules;
        try {
            modules = account.getCourses()
                    .get(calendarIndex)
                    .getTabs()
                    .get(tabIndex)
                    .getModules();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        for(Module module : modules) {
            urls.add(module.getUrl());
        }

        return urls;
    }

    public ArrayList<Boolean> getCertainties(int calendarIndex, int tabIndex) {
        ArrayList<Boolean> certainties = new ArrayList<>();
        ArrayList<Module> modules;
        try {
            modules = account.getCourses()
                    .get(calendarIndex)
                    .getTabs()
                    .get(tabIndex)
                    .getModules();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        for(Module module : modules) {
            certainties.add(module.getType().equals("Assignment"));
        }

        return certainties;
    }
}
