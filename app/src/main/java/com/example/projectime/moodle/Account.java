package com.example.projectime.moodle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Account {
    private static final String URL_FORMAT_LOGIN = "%s://%s/login/index.php";

    private static final String CSS_LOGIN_FORM = "form#login";
    private static final String CSS_USERNAME_FIELD = "input#username";
    private static final String CSS_PASSWORD_FIELD = "input#password";

    private static final String URL_FORMAT_HOME = "%s://%s";

    private static final String CSS_COURSE_CARD_CONTAINER = "div#snap-pm-courses-current-cards";
    private static final String CSS_COURSE_CARDS = "div.coursecard";
    private static final String CSS_COURSE_NAME_ANCHOR = "a.coursecard-coursename";

    private static final String ATTR_COURSE_ID = "data-courseid";

    private static final String COOKIE_AUTH_TOKEN = "MoodleSession";

    private static final long UPDATE_INTERVAL_COURSE = 3600000L;

    private ArrayList<Course> courses = new ArrayList<>();
    private long lastCourseUpdate = 0;

    private String instanceProtocol;
    private String instanceDomain;
    private String username;
    private String password;

    private String authToken;

    public Account(String instanceURL, String username, String password) throws IOException {
        parseURL(instanceURL);
        this.username = username;
        this.password = password;

        this.authenticate();
    }

    public String authenticate() throws IOException {
        FormElement loginForm = getLoginForm();

        Element usernameTextBox = loginForm.selectFirst(CSS_USERNAME_FIELD);
        Element passwordTextBox = loginForm.selectFirst(CSS_PASSWORD_FIELD);

        usernameTextBox.val(this.username);
        passwordTextBox.val(this.password);

        Connection.Response loginResponse = loginForm.submit()
                .method(Connection.Method.POST)
                .execute();
        String authToken = loginResponse.cookie(COOKIE_AUTH_TOKEN);

        this.authToken = authToken;

        return authToken;
    }

    public ArrayList<Course> getCourses() throws IOException {
        if(!isCoursesUpdated()) {
            updateCourses();
        }
        return courses;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private FormElement getLoginForm () throws IOException {
        String loginPageURL = String.format(URL_FORMAT_LOGIN, instanceProtocol, instanceDomain);
        Document loginPage = Jsoup.connect(loginPageURL).get();
        return (FormElement)loginPage.selectFirst(CSS_LOGIN_FORM);
    }

    private boolean isCoursesUpdated() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastUpdate = currentTime - lastCourseUpdate;
        return timeSinceLastUpdate <= UPDATE_INTERVAL_COURSE;
    }

    private void parseURL(String instanceURL) throws MalformedURLException {
        try {
            tryParseURL(instanceURL);
        } catch (MalformedURLException mue) {
            tryParseURL("https://"+instanceURL);
        }
    }

    private void tryParseURL(String instanceURL) throws MalformedURLException {
        URL parsedURL = new URL(instanceURL);
        this.instanceDomain = parsedURL.getHost();
        this.instanceProtocol = parsedURL.getProtocol();
        if (!this.instanceProtocol.equals("http")) {
            this.instanceProtocol = "https";
        }
    }

    private void updateCourses() throws IOException {
        Elements courseCards = getCourseCards();
        ArrayList<Integer> ids = new ArrayList<>();
        for(Element courseCard : courseCards) {
            Element courseNameAnchor = courseCard.selectFirst(CSS_COURSE_NAME_ANCHOR);

            int id = Integer.parseInt(courseCard.attr(ATTR_COURSE_ID));
            String url = courseNameAnchor.attr("href");
            String name = courseNameAnchor.text();

            boolean isInList = false;

            for(Course course : courses) {
                if(course.getID() == id) {
                    isInList = true;
                    break;
                }
            }
            if(!isInList) {
                courses.add(new Course(this, id, url, name));
            }

            ids.add(id);
        }
        for(Course course : courses) {
            if(!ids.contains(course.getID())) {
                courses.remove(course);
            }
        }

        lastCourseUpdate = System.currentTimeMillis();
    }

    private Elements getCourseCards() throws IOException {
        String homeURL = String.format(URL_FORMAT_HOME, instanceProtocol, instanceDomain);
        Document homePage = Jsoup.connect(homeURL)
                .cookie(COOKIE_AUTH_TOKEN, authToken).get();
        Element courseCardContainer = homePage.selectFirst(CSS_COURSE_CARD_CONTAINER);
        return courseCardContainer.select(CSS_COURSE_CARDS);
    }
}