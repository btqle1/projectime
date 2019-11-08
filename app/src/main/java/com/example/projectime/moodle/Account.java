package com.example.projectime.moodle;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Account {
    private static final String URL_FORMAT_LOGIN = "%s://%s/login/index.php";

    private static final String CSS_LOGIN_FORM = "form#login";
    private static final String CSS_USERNAME_FIELD = "input#username";
    private static final String CSS_PASSWORD_FIELD = "input#password";

    private static final String COOKIE_AUTH_TOKEN = "MoodleSession";

    private String instanceProtocol;
    private String instanceDomain;
    private String username;
    private String password;

    private String authToken;

    public Account(String instanceUrl, String username, String password) throws MalformedURLException {
        parseUrl(instanceUrl);
        this.username = username;
        this.password = password;
    }

    private void parseUrl(String instanceUrl) throws MalformedURLException {
        try {
            tryParseUrl(instanceUrl);
        } catch (MalformedURLException mue) {
            tryParseUrl("http://"+instanceUrl);
        }
    }

    private void tryParseUrl(String instanceUrl) throws MalformedURLException {
        URL parsedUrl = new URL(instanceUrl);
        this.instanceDomain = parsedUrl.getHost();
        this.instanceProtocol = parsedUrl.getProtocol();
        if (!this.instanceProtocol.equals("http")) {
            this.instanceProtocol = "https";
        }
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

    private FormElement getLoginForm () throws IOException {
        String loginPageURL = String.format(URL_FORMAT_LOGIN, instanceProtocol, instanceDomain);
        Document homePage = Jsoup.connect(loginPageURL).get();
        return (FormElement)homePage.selectFirst(CSS_LOGIN_FORM);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
