package com.ericliudeveloper.sharedbillhelper.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ericliudeveloper.sharedbillhelper.R;

/**
 * Created by liu on 6/06/15.
 */
public class BaseActivity extends AppCompatActivity {
    protected android.support.v4.widget.DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected ViewPager mViewPager;
    protected TabLayout mTabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        if (mToolbar != null) {
            setupToolbar();
        }

        if (mDrawerLayout != null) {
            setupNavigationDrawer();
        }

        if (mToolbar != null && mDrawerLayout != null) {
            setupDrawerToggle();
        }

        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }

        if (mTabLayout != null && mViewPager != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }

    }


    private void setupDrawerToggle() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);

        actionBarDrawerToggle.syncState();
    }

    private void setupToolbar() {
//        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupNavigationDrawer() {
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

    }

    /**
     * For subclasses which have Viewpagers,
     * they should override this method to setup the Pager Adapter
     */
    protected void setupViewPager(ViewPager pager) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
