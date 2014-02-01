package com.flaminga.client.authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.flaminga.client.Flaminga;
import com.flaminga.client.KeyConstant;
import com.flaminga.client.R;
import com.flaminga.client.activity.TwitterAuthenticationActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


/**
 * Manages authenticating through Twitter.
 */
public class TwitterAuthenticationManager {

    private static final String TAG = "TwitterAuthenticationManager";
    public static final int TWITTER_REQUEST = 5; // Ensure this is unique. TODO: Place in res

    private static final String REQUEST_TOKEN = "request_token";
    private static final String ACTIVITY = "activity";

    private static Twitter sTwitter;
    private static RequestToken sRequestToken;
    private static WeakReference<TwitterAuthenticationListener> sListener;

    private TwitterAuthenticationManager() {}

    public static void setOnTwitterAuthenticationListener(TwitterAuthenticationListener listener) {
        sListener = new WeakReference<TwitterAuthenticationListener>(listener);
    }

    /**
     *
     * @param activity // The activity that launched the launchRequest call. If done by a fragment, have that call getActivity()
     */
    public static void launchRequest(Activity activity) {

        new AsyncTask<Activity, Void, HashMap<String, Object>>() {

            @Override
            protected HashMap<String, Object> doInBackground(Activity... params) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(ACTIVITY, params[0]);

                sTwitter = new TwitterFactory().getInstance();
                sTwitter.setOAuthConsumer(KeyConstant.TWITTER_CONSUMER_KEY, KeyConstant.TWITTER_CONSUMER_SECRET);

                // Construct callback url.
                String oauthCallbackUrl = Flaminga.getFlamingaString(R.string.oauth_callback);

                try {
                    map.put(REQUEST_TOKEN, sTwitter.getOAuthRequestToken(oauthCallbackUrl));
                } catch (TwitterException e) {
                    // Potentially an error due to having a bad API key. Check for and update
                    map.put(REQUEST_TOKEN, null);
                    Log.e(TAG, e.getMessage(), e);
                }
                return map;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> result) {
                TwitterAuthenticationListener listener = sListener == null ? null : sListener.get();
                if (result == null) {
                    if (listener != null) {
                        listener.onTwitterAuthentication(Flaminga.getFlamingaString(R.string.twitter_error), null, null);
                    }
                    return;
                }
                Activity activity = (Activity) result.get(ACTIVITY);
                RequestToken requestToken = (RequestToken) result.get(REQUEST_TOKEN);

                if (requestToken == null) {
                    if (listener != null) {
                        listener.onTwitterAuthentication(Flaminga.getFlamingaString(R.string.twitter_error), null, null);
                    }
                    return;
                }

                // Save requestToken
                sRequestToken = requestToken;

                // Now that we have the request token, launch the OAuth webview to allow signin.
                if (activity == null) return;
                if (activity != null && !activity.isFinishing()) {
                    Intent intent = new Intent(activity.getApplicationContext(), TwitterAuthenticationActivity.class);
                    String authorizationUrl = Uri.parse(requestToken.getAuthorizationURL()).toString();
                    intent.putExtra(TwitterAuthenticationActivity.URL, authorizationUrl);
                    activity.startActivityForResult(intent, TWITTER_REQUEST);
                }
            }
        }.execute(activity);
    }



    /**
     *
     * @param verifier
     * @param activity // This feels hacky as hell, but throwing it in here for now.
     */
    public static void verifyRequestToken(final String verifier, final TwitterAuthenticationActivity activity) {
        if (verifier == null || sTwitter == null || sRequestToken == null) {
            TwitterAuthenticationListener listener = sListener == null ? null : sListener.get();
            if (listener != null) {
                listener.onTwitterAuthentication(Flaminga.getFlamingaString(R.string.twitter_error), null, null);
            }
            return;
        }

        new AsyncTask<String, Void, HashMap<String, Object>>() {

            private final static String NOTIFICATION = "notification";
            private final static String ACCESS_TOKEN = "access_token";
            private final static String TOKEN_SECRET = "secret_token";

            @Override
            protected HashMap<String, Object> doInBackground(String... params) {
                HashMap<String, Object> result = new HashMap<String, Object>();

                try {
                    AccessToken accessToken = sTwitter.getOAuthAccessToken(sRequestToken, params[0]);
                    String accessTokenString;
                    if ((accessToken != null) && (accessTokenString = accessToken.getToken()) != null && (accessTokenString.length() > 0)) {
                        // Set Twitter tokens
                        result.put(ACCESS_TOKEN, accessTokenString);
                        result.put(TOKEN_SECRET, accessToken.getTokenSecret());
                    } else {
                        result.put(NOTIFICATION, Flaminga.getFlamingaString(R.string.twitter_error));
                    }
                } catch (TwitterException e) {
                    result.put(NOTIFICATION, Flaminga.getFlamingaString(R.string.twitter_error));
                    Log.e(TAG, e.getMessage(), e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> result) {
                TwitterAuthenticationListener listener = (sListener == null) ? null : sListener.get();
                if (result == null) {
                    if (listener != null) {
                        listener.onTwitterAuthentication(Flaminga.getFlamingaString(R.string.twitter_error), null, null);
                    }
                    return;
                }

                String notification = (String) result.get(NOTIFICATION);
                String accessToken = (String) result.get(ACCESS_TOKEN);
                String tokenSecret = (String) result.get(TOKEN_SECRET);

                if (listener != null) {
                    listener.onTwitterAuthentication(notification, accessToken, tokenSecret);
                    activity.finish();
                }
            }
        }.execute(verifier);
    }

    public static interface TwitterAuthenticationListener {
        void onTwitterAuthentication(String notification, final String accessToken, final String accessTokenSecret);
    }

}
