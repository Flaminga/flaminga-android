package com.flaminga.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.flaminga.client.Flaminga;
import com.flaminga.client.authentication.TwitterAuthenticationManager;
import com.flaminga.client.model.Account;

/**
 * The launcher activity for Flaminga. Will determine what should be the first activity shown to the
 * user, and then launch it.
 *
 * And yes, ideally would put most/all of this in a fragment, but given that we're never displaying
 * anything, and I'm in a rush, keeping it in the activity for now.
 *
 * Created by mjanes on 1/31/14.
 */
public class LaunchActivity extends Activity implements TwitterAuthenticationManager.TwitterAuthenticationListener {

    private static final String TAG = "LaunchActivity";

    /***********************************************************************************************
     * Lifecycle methods
     **********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthenticationManager.setOnTwitterAuthenticationListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchNext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TwitterAuthenticationManager.setOnTwitterAuthenticationListener(null);
    }


    /***********************************************************************************************
     * Utility
     **********************************************************************************************/

    protected Flaminga getApp() {
        return (Flaminga) getApplication();
    }

    /**
     * After the launch activity is loaded, determine and start the activity that should be
     * displayed first to the user.
     */
    protected void launchNext() {
        // Logic:
        // If there are no accounts, go to TwitterAuthenticationActivity
        // For now, the MVP, I'm going straight to the TwitterAuthenticationActivity, we'll deal
        // with handling multiple accounts, and previously signed in accounts after we get this
        // working.
        //
        // Temporarily divert through the TwitterAuthenticationManager/Activity and then, once that
        // is done, bounce to the next activity. Hrm... should be a cleaner way of doing this, but
        // pasting together what we have.

        // TODO, this is only checking for whether or not an account exists. Not, whether or not the
        // account has a valid access token.
        if (Flaminga.getAccountCount() == 0) {
            TwitterAuthenticationManager.launchRequest(LaunchActivity.this);
        } else {
            Intent intent = new Intent(this, NavDrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Called on return
     * @param notification
     * @param accessToken
     * @param accessTokenSecret
     */
    @Override
    public void onTwitterAuthentication(String notification, String accessToken, String accessTokenSecret) {
        if (notification == null) {
            Account newAccount = new Account(accessToken, accessTokenSecret);
            Flaminga.addAccount(newAccount);
            launchNext();
        } else {
            Log.e(TAG, "Error authenticating with Twitter: " + null);
        }
    }
}
