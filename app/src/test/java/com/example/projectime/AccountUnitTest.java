package com.example.projectime;

import com.example.projectime.moodle.Account;
import com.example.projectime.moodle.Course;
import com.example.projectime.moodle.Tab;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AccountUnitTest {
    private static final int LOGIN_TOKEN_LENGTH = 32;

    @Test
    public void testLoginTokenLength() throws IOException {
        String instanceURL = LoginStrings.instanceURL;
        String username = LoginStrings.username;
        String password = LoginStrings.password;

        Account account = new Account(instanceURL, username, password);
        String authToken = account.authenticate();

        assertEquals(LOGIN_TOKEN_LENGTH, authToken.length());
    }

    @Test
    public void testCourseCount() throws IOException {
        String instanceURL = LoginStrings.instanceURL;
        String username = LoginStrings.username;
        String password = LoginStrings.password;

        Account account = new Account(instanceURL, username, password);
        ArrayList<Course> courses = account.getCourses();

        assertEquals(LoginStrings.courseCount, courses.size());
    }

    @Test
    public void testTabList() throws IOException {
        String instanceURL = LoginStrings.instanceURL;
        String username = LoginStrings.username;
        String password = LoginStrings.password;

        Account account = new Account(instanceURL, username, password);
        ArrayList<Course> courses = account.getCourses();
        ArrayList<Tab> firstCourseTabs = courses.get(0).getTabs();

        assertEquals(LoginStrings.firstCourseTabCount, firstCourseTabs.size());
    }

    @Test
    public void testTabList2() throws IOException {
        String instanceURL = LoginStrings.instanceURL;
        String username = LoginStrings.username;
        String password = LoginStrings.password;

        Account account = new Account(instanceURL, username, password);
        ArrayList<Course> courses = account.getCourses();
        ArrayList<Tab> secondCourseTabs = courses.get(1).getTabs();

        assertEquals(LoginStrings.secondCourseTabCount, secondCourseTabs.size());
    }
}
