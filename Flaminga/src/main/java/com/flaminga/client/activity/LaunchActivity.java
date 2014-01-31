package com.flaminga.client.activity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.flaminga.client.Flaminga;
import com.flaminga.client.R;

public class LaunchActivity extends Activity {

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
        // I


    }
}
