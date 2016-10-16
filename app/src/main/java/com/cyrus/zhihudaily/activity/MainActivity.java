package com.cyrus.zhihudaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    private long mFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFragment();
        initData();
    }

    private void initData() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(this, mDlMenu, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mToggle.syncState();
        mDlMenu.addDrawerListener(mToggle);
        initNavigation();
    }

    private void initNavigation() {
        mNvDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_favorite:
                        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_about:
                        //TODO 点击关于菜单项的响应事件
                        return true;
                }
                return false;
            }
        });
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_content, mNlfNewsList)
                .commit();
    }

    private void initView() {
        mNlfNewsList = new NewsListFragment();
        mDlMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mNvDrawer = (NavigationView) findViewById(R.id.nv_drawer);
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
    }

    @Override
    public void onBackPressed() {
        if (mDlMenu.isDrawerOpen(GravityCompat.START)) {
            mDlMenu.closeDrawers();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - mFirstTime > 2000) {
                Snackbar.make(mFlContent, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                mFirstTime = secondTime;
            } else {
                finish();
            }
        }
    }

}
