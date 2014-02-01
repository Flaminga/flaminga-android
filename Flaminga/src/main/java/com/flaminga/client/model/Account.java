package com.flaminga.client.model;

/**
 * Currently we are going to be storing this in memory, probably serialize it and store it in a
 * shared preference. However, if we get to the point that we're storing block lists and mute lists
 * on the phone instead of a server, we may want to make this into a db object.
 *
 * Created by mjanes on 1/30/14.
 */
public class Account {

    private static final String KEY_ID = "id";
    private static final String KEY_SCREEN_NAME = "screenName";
    private static final String KEY_NAME = "name";
    private static final String KEY_OAUTH_TOKEN = "oAuthToken";
    private static final String KEY_OAUTH_SECRET = "oAuthSecret";

    private String mOAuthToken;
    private String mOAuthSecret;

    public Account() {}

    public Account(String oAuthToken, String oAuthSecret) {
        mOAuthToken = oAuthToken;
        mOAuthSecret = oAuthSecret;
    }
}
