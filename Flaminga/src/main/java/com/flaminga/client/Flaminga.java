package com.flaminga.client;

import android.app.Application;
import android.content.Context;

import com.flaminga.client.model.Account;

import java.util.ArrayList;

/**
 * Created by mjanes on 1/30/14.
 */
public class Flaminga extends Application {

    private static Context mContext; // Watch and make sure no memory leak

    // Question: How are we going to store this permanently? Shared preferences?
    // Make Accounts serializable then?
    private ArrayList<Account> mAccounts = new ArrayList<Account>();


    /***********************************************************************************************
     * Lifecycle methods
     **********************************************************************************************/

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    /***********************************************************************************************
     * Account handling
     **********************************************************************************************/

    public void addAccount(Account account) {
        mAccounts.add(account);
    }

    public ArrayList<Account> getAccounts() {

        return mAccounts;
    }

    public int getAccountCount() {
        return mAccounts.size();
    }


    /***********************************************************************************************
     * Resources access
     **********************************************************************************************/

    public static String getFlamingaString(int id) {
        return mContext.getResources().getString(id);
    }
}


