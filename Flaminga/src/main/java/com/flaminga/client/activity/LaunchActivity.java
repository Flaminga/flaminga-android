package com.flaminga.client.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.flaminga.client.Flaminga;
import com.flaminga.client.R;
import com.flaminga.client.authentication.TwitterAuthenticationManager;

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
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        launchNext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchNext();
    }


    /***********************************************************************************************
     * Utility
     **********************************************************************************************/

    protected Flaminga getFlamingaApplication() {
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
        TwitterAuthenticationManager.getRequestToken(LaunchActivity.this);
    }


    /***********************************************************************************************
     * Utilities/callbacks/etc.
     **********************************************************************************************/

    /**
     * Called on return
     * @param notification
     * @param accessToken
     * @param accessTokenSecret
     */
    @Override
    public void onTwitterAuthentication(String notification, String accessToken, String accessTokenSecret) {
        if (notification == null) {
            // TODO: Store the access tokens in whatever format we're using for the accounts
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.e(TAG, "Error authenticating with Twitter: " + null);
        }
    }
}
