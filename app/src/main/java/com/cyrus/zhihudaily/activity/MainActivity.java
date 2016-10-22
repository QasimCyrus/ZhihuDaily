package com.cyrus.zhihudaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.SharePreferenceConstant;
import com.cyrus.zhihudaily.database.DetailDB;
import com.cyrus.zhihudaily.database.NewsDB;
import com.cyrus.zhihudaily.fragment.NewsListFragment;

import java.io.File;

public class MainActivity extends BaseActivity {

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
        mNvDrawer.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_favorite:
                        startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                        return true;
                    case R.id.nav_clear:
                        //清除SharePreference缓存
                        getBaseApplication().getSp().edit().clear().apply();
                        getSharedPreferences(SharePreferenceConstant
                                .NEWS_PREFERENCE_NAME, MODE_PRIVATE).edit().clear().apply();
                        //清除数据库缓存
                        new NewsDB().deleteAll();
                        new DetailDB().deleteAll();
                        //清除Picasso图片库缓存
                        File file = new File(getCacheDir().getPath());
                        boolean isSuccessful = deleteAllFile(file);
                        Snackbar.make(mFlContent, isSuccessful ? "删除成功" : "删除失败",
                                Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 删除所有文件
     *
     * @param file 要删除的文件或文件夹
     * @return 删除成功返回true，删除失败返回false
     */
    private boolean deleteAllFile(File file) {
        boolean isSuccessful;

        if (file.exists()) {
            if (file.isFile()) {//如果是文件，直接删除
                isSuccessful = file.delete();
                return isSuccessful;
            } else if (file.isDirectory()) {//如果是文件夹，递归删除
                File files[] = file.listFiles();
                if (files.length == 0 ) {
                    return file.delete();
                } else {
                    for (File file1 : files) {
                        deleteAllFile(file1);
                    }
                    return file.delete();
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, mNlfNewsList)
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
