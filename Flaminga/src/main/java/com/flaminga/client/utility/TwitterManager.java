package com.flaminga.client.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.flaminga.client.Flaminga;
import com.flaminga.client.KeyConstant;
import com.flaminga.client.model.Account;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Manages directly talking to Twitter. May possibly involve talking to Flaminga
 *
 * Created by mjanes on 2/2/14.
 */
public class TwitterManager {

    public static final String TAG = "TwitterManager";

    final Twitter mTwitter;

    public TwitterManager() {

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(KeyConstant.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(KeyConstant.TWITTER_CONSUMER_SECRET);

        // Access Token
        Account account = Flaminga.getAccount();
        String accessToken = account.getAccessToken();
        // Access Token Secret
        String accessTokenSecret = account.getAccessSecret();

        AccessToken fullAccessToken = new AccessToken(accessToken,accessTokenSecret);
        mTwitter = new TwitterFactory(builder.build()).getInstance(fullAccessToken);
    }

    public void retweet(long id) {
        new AsyncTask<Long, Void, Void>() {

            @Override
            protected Void doInBackground(Long... longs) {
                try {
                    mTwitter.retweetStatus(longs[0]);
                } catch (TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }
        }.execute(id);

    }

    /**
     * Favorite
     */
    public void favorite(long id) {
        new AsyncTask<Long, Void, Void>() {

            @Override
            protected Void doInBackground(Long... longs) {
                try {
                    mTwitter.createFavorite(longs[0]);
                } catch (TwitterException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }
        }.execute(id);
    }
}
