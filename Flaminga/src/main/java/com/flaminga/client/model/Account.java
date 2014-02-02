package com.flaminga.client.model;

/**
 * Currently we are going to be storing this in memory, probably serialize it and store it in a
 * shared preference. However, if we get to the point that we're storing block lists and mute lists
 * on the phone instead of a server, we may want to make this into a db object.
 *
 * Created by mjanes on 1/30/14.
 */
public class Account {

    private String mAccessToken;
    private String mAccessSecret;

    public Account() {}

    public Account(String accessToken, String accessSecret) {
        mAccessToken = accessToken;
        mAccessSecret = accessSecret;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getAccessSecret() {
        return mAccessSecret;
    }
}
