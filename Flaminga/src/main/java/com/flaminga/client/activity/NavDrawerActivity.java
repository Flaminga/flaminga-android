package com.flaminga.client.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.flaminga.client.fragment.SettingsFragment;
import com.flaminga.client.fragment.TimelineFragment;
import com.flaminga.client.R;

import java.util.ArrayList;

/**
 * NavDrawerActivity, displays HomeFragment
 *
 * Created by mjanes on 1/30/14.
 */
public class NavDrawerActivity extends FragmentActivity {

    // Util
    private ActionBarDrawerToggle mDrawerToggle;

    // Widgets
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerContainer;
    private ListView mDrawerList;
    private MenuListAdapter mMenuListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_dawer);

        // Drawer layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Drawer Container
        mDrawerContainer = (LinearLayout) findViewById(R.id.left_drawer);

        // Get the menu list
        mDrawerList = (ListView) findViewById(R.id.menu_list);
        mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setupAdapter();
        mDrawerList.setAdapter(mMenuListAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemListener());


        // Action bar drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){
            @Override
            public void onDrawerClosed(View view) {
                // TODO: update action bar
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // TODO: update action bar
                invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            loadTimelineFragment();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    /***********************************************************************************************
     * Handling the sidedrawer
     **********************************************************************************************/

    private static class MenuListAdapter extends ArrayAdapter<Integer> {
        public MenuListAdapter(Context context, ArrayList<Integer> menuTitles){
            super(context, 0, menuTitles);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent){
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(getItem(pos), parent, false);
            }
            return convertView;
        }
    }

    /**
     * Sets up the menu's MenuListAdapter
     * @return
     */
    private void setupAdapter() {
        ArrayList<Integer> menuItems = new ArrayList<Integer>();
        menuItems.add(R.layout.menu_item_timeline);
        menuItems.add(R.layout.menu_item_settings);
        mMenuListAdapter = new MenuListAdapter(this, menuItems);
    }

    private class DrawerItemListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id){
            if (position == 0) {
                loadTimelineFragment();
            } else if (position == 1) {
                loadSettingsFragment();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /***********************************************************************************************
     * Action bar
     **********************************************************************************************/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerContainer);
        int size = menu.size();
        for (int i = 0; i < size; i ++) {
            menu.getItem(i).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Must call this so that the fragment's onOptionsItemSelected() is called
        return super.onOptionsItemSelected(item);
    }


    /***********************************************************************************************
     * Individual fragment transactions
     **********************************************************************************************/

    public void loadTimelineFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof TimelineFragment)) {
            handleFragmentTransaction(fm, TimelineFragment.newInstance());
        }
        resetDrawer(0);
    }

    public void loadSettingsFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof SettingsFragment)) {
            handleFragmentTransaction(fm, SettingsFragment.newInstance());
        }
        resetDrawer(1);
    }

    /**
     * Takes care of fragment transactions, including updating the ActionBar's title
     * and closing the drawer.
     *
     * @param fragment
     */
    private void handleFragmentTransaction(FragmentManager fm, Fragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private void resetDrawer(int positionToCheck) {
        mDrawerList.setItemChecked(positionToCheck, true);

        if (mDrawerLayout.isDrawerOpen(mDrawerContainer)) {
            mDrawerLayout.closeDrawer(mDrawerContainer);
        } else {
            invalidateOptionsMenu();
        }
    }

}
