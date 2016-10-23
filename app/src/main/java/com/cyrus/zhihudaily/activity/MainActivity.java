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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.constants.SharePreferenceConstant;
import com.cyrus.zhihudaily.database.NewsDetailDB;
import com.cyrus.zhihudaily.database.NewsDB;
import com.cyrus.zhihudaily.database.ThemeNewsDB;
import com.cyrus.zhihudaily.fragment.CategoryNewsFragment;
import com.cyrus.zhihudaily.fragment.HomeNewsFragment;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.utils.UiUtils;

import java.io.File;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDlMenu;
    private Toolbar mToolbar;
    private NavigationView mNvDrawer;
    private FrameLayout mFlHomeContent;
    private HomeNewsFragment mNlfNewsList;
    private CategoryNewsFragment mCnfNewsList1;
    private CategoryNewsFragment mCnfNewsList2;
    private Bundle mBundle;
    private long mFirstTime;
    private boolean mFirstCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initHomeFragment();
        initData();
    }

    private void initData() {
        mBundle = new Bundle();
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDlMenu, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();
        mDlMenu.addDrawerListener(toggle);
        initNavigation();
    }

    private void initNavigation() {
        mNvDrawer.setItemBackgroundResource(R.drawable.menu_item_bg);
        mNvDrawer.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (mDlMenu.isDrawerOpen(GravityCompat.START)) {
                    mDlMenu.closeDrawers();
                }
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        initHomeFragment();
                        return true;
                    case R.id.nav_favorite:
                        startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                        return true;
                    case R.id.nav_comic:
                        initCategoryFragment(ThemeId.COMIC_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_company:
                        initCategoryFragment(ThemeId.COMPANY_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_design:
                        initCategoryFragment(ThemeId.DESIGN_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_finance:
                        initCategoryFragment(ThemeId.FINANCE_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_game:
                        initCategoryFragment(ThemeId.GAME_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_internet:
                        initCategoryFragment(ThemeId.INTERNET_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_movie:
                        initCategoryFragment(ThemeId.MOVIE_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_music:
                        initCategoryFragment(ThemeId.MUSIC_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_not_boring:
                        initCategoryFragment(ThemeId.NOT_BORING_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_psychology:
                        initCategoryFragment(ThemeId.PSYCHOLOGY_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_recommend:
                        initCategoryFragment(ThemeId.RECOMMEND_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_sport:
                        initCategoryFragment(ThemeId.SPORT_ID, item.getTitle().toString());
                        return true;
                }
                return false;
            }
        });
    }

    private void initCategoryFragment(String id, String title) {
        mToolbar.setTitle(title);
        mBundle.putString(DataConstant.BUNDLE_THEME_ID, id);
        mFirstCategoryFragment = !mFirstCategoryFragment;

        //用两个Fragment轮流显示主题新闻，因为不能多次setArguments()
        if (mFirstCategoryFragment) {
            mCnfNewsList1.setArguments(mBundle);
        } else {
            mCnfNewsList2.setArguments(mBundle);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, mFirstCategoryFragment
                        ? mCnfNewsList1
                        : mCnfNewsList2)
                .commit();
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
                if (files.length == 0) {
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

    private void initHomeFragment() {
        mToolbar.setTitle(R.string.navigation_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, mNlfNewsList)
                .commit();
    }

    private void initView() {
        mNlfNewsList = new HomeNewsFragment();
        mCnfNewsList1 = new CategoryNewsFragment();
        mCnfNewsList2 = new CategoryNewsFragment();
        mDlMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mNvDrawer = (NavigationView) findViewById(R.id.nv_drawer);
        mFlHomeContent = (FrameLayout) findViewById(R.id.fl_content);
    }

    @Override
    public void onBackPressed() {
        if (mDlMenu.isDrawerOpen(GravityCompat.START)) {
            mDlMenu.closeDrawers();
        } else {
            long secondTime = System.currentTimeMillis();
            if (secondTime - mFirstTime > 2000) {
                Snackbar.make(mFlHomeContent, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                mFirstTime = secondTime;
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDlMenu.isDrawerOpen(GravityCompat.START)) {
            mDlMenu.closeDrawers();
        }
        switch (item.getItemId()) {
            case R.id.menu_item_clear_cache:
                ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //清除SharePreference缓存
                        getBaseApplication().getSp().edit().clear().apply();
                        getSharedPreferences(SharePreferenceConstant
                                .NEWS_PREFERENCE_NAME, MODE_PRIVATE).edit().clear().apply();

                        //清除数据库缓存
                        new NewsDB().deleteAll();
                        new NewsDetailDB().deleteAll();
                        new ThemeNewsDB().deleteAll();

                        //清除cache文件夹缓存
                        File file = new File(getCacheDir().getPath());
                        final boolean isSuccessful = deleteAllFile(file);

                        UiUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mNlfNewsList.onRefresh();
                                Snackbar.make(mFlHomeContent,
                                        isSuccessful ? "清除缓存成功" : "清除缓存失败",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                return true;
            case R.id.menu_item_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 存放新闻主题ID
     */
    private class ThemeId {
        private static final String GAME_ID = "2";
        private static final String MOVIE_ID = "3";
        private static final String DESIGN_ID = "4";
        private static final String COMPANY_ID = "5";
        private static final String FINANCE_ID = "6";
        private static final String MUSIC_ID = "7";
        private static final String SPORT_ID = "8";
        private static final String COMIC_ID = "9";
        private static final String INTERNET_ID = "10";
        private static final String NOT_BORING_ID = "11";
        private static final String RECOMMEND_ID = "12";
        private static final String PSYCHOLOGY_ID = "13";
    }
}
