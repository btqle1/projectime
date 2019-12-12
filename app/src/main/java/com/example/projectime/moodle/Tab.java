package com.example.projectime.moodle;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Tab {
    private static final String[] MONTHS = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    private static final String CSS_MODULES = "div.content > ul.section.img-text > li";
    private static final String CSS_MODULE_NAME = "p.instancename";
    private static final String CSS_MODULE_DATE = "a.snap-due-date";

    private static final String ATTR_ID = "id";
    private static final String ATTR_URL = "data-href";
    private static final String ATTR_TYPE = "data-type";

    private Course course;

    private HashMap<String, Module> moduleLookup = new HashMap<>();
    private ArrayList<Module> modules = new ArrayList<>();

    private String id;
    private String name;

    public Tab(Course course, String id, String name) {
        this.course = course;
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateModules(Element tabElement) {
        Elements moduleElements = tabElement.select(CSS_MODULES);
        HashMap<String, Module> newModuleLookup = new HashMap<>();
        ArrayList<Module> newModules = new ArrayList<>();
        for(Element moduleElement : moduleElements) {
            Element nameElement = moduleElement.selectFirst(CSS_MODULE_NAME);

            String id = moduleElement.attr(ATTR_ID);
            String name = "";
            if(nameElement != null)
                name = nameElement.text();

            String url = moduleElement.attr(ATTR_URL);
            String type = moduleElement.attr(ATTR_TYPE);

            Calendar dueDate = new GregorianCalendar();

            dueDate.setTime(course.getStartDate());

            try {
                Element dateElement = moduleElement.selectFirst(CSS_MODULE_DATE);
                String date = dateElement.text();

                for(int i = 0; i < MONTHS.length; i++) {
                    int x = date.indexOf(MONTHS[i]);
                    if(x != -1) {
                        String[] sections = date.substring(x).split(" ");
                        int day = Integer.parseInt(sections[1].replace(",",""));
                        int year = Integer.parseInt(sections[2]);
                        dueDate = new GregorianCalendar(year, i, day);
                    }
                }
            } catch (Exception e) {}

            Module module = null;
            if(moduleLookup.containsKey(id)) {
                module = moduleLookup.get(id);
            } else {
                module = new Module(this, id, name, url, type, dueDate.getTimeInMillis());
            }
            newModules.add(module);
            newModuleLookup.put(id, module);
        }
        modules = newModules;
        moduleLookup = newModuleLookup;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
