package com.example.projectime;

import com.example.projectime.moodle.Account;
import com.example.projectime.moodle.Course;

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

        assertEquals(authToken.length(), LOGIN_TOKEN_LENGTH);
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
}
