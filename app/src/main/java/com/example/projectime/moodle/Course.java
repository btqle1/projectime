package com.example.projectime.moodle;

import android.util.SparseArray;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Course {
    private static final String CSS_COURSE_CONTENT = "div.course-content > ul.topics > li";

    private static final String ATTR_TAB_ID = "id";
    private static final String ATTR_TAB_NAME = "aria-label";

    private static final long UPDATE_INTERVAL_TAB = 3600000L;
    private long lastTabUpdate = 0;

    private Account account;

    private HashMap<String,Tab> tabLookup = new HashMap<>();
    private ArrayList<Tab> tabs = new ArrayList<>();

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

    public ArrayList<Tab> getTabs() throws IOException {
        if(!isTabsUpdated()) {
            updateTabs();
        }
        return tabs;
    }

    private void updateTabs() throws IOException {
        Document coursePage = account.getConnection(url).get();
        Elements tabElements = coursePage.select(CSS_COURSE_CONTENT);
        HashMap<String,Tab> newTabLookup = new HashMap<>();
        ArrayList<Tab> newTabs = new ArrayList<>();
        for(Element tabElement : tabElements) {
            String id = tabElement.attr(ATTR_TAB_ID);
            String name = tabElement.attr(ATTR_TAB_NAME);

            Tab currentTab = null;
            if(tabLookup.containsKey(id)) {
                currentTab = tabLookup.get(id);
            } else {
                currentTab = new Tab(this, id, name);
            }
            newTabs.add(currentTab);
            newTabLookup.put(id, currentTab);

            currentTab.updateModules(tabElement);
        }
        tabs = newTabs;
        tabLookup = newTabLookup;

        lastTabUpdate = System.currentTimeMillis();
    }

    private boolean isTabsUpdated() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastUpdate = currentTime - lastTabUpdate;
        return timeSinceLastUpdate <= UPDATE_INTERVAL_TAB;
    }
}
