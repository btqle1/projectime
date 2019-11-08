package com.example.projectime;

import com.example.projectime.moodle.Account;

import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class AccountUnitTest {
    @Test
    public void testLoginTokenLength() throws IOException {
        String instanceUrl = LoginStrings.instanceUrl;
        String username = LoginStrings.username;
        String password = LoginStrings.password;

        Account account = new Account(instanceUrl, username, password);
        String authToken = account.authenticate();

        assertEquals(authToken.length(), 32);
    }
}
