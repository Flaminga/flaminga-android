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
    private static ArrayList<Account> mAccounts = new ArrayList<Account>();


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

    public static void addAccount(Account account) {
        mAccounts.add(account);
    }

    public static ArrayList<Account> getAccounts() {

        return mAccounts;
    }

    /**
     * Only for quick functionality
     */
    public static Account getAccount() {
        return mAccounts.get(0);
    }

    public static int getAccountCount() {
        return mAccounts.size();
    }


    /***********************************************************************************************
     * Resources access
     **********************************************************************************************/

    public static String getFlamingaString(int id) {
        return mContext.getResources().getString(id);
    }
}


