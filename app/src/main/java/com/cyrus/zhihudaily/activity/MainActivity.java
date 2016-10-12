package com.cyrus.zhihudaily.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.fragment.NewsListFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDlMenu;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private NavigationView mNvDrawer;
    private FrameLayout mFlContent;
    private NewsListFragment mNlfNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, mNlfNewsList)
                .commit();
    }

    private void initView() {
        mNlfNewsList = new NewsListFragment();
        mDlMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNvDrawer = (NavigationView) findViewById(R.id.nv_drawer);
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
    }
}
