package com.cyrus.zhihudaily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import com.cyrus.zhihudaily.database.NewsDB;
import com.cyrus.zhihudaily.database.NewsDetailDB;
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
    private FragmentManager mFragmentManager;
    private HomeNewsFragment mHomeNewsFragment;
    private CategoryNewsFragment mCategoryNewsFragment1;
    private CategoryNewsFragment mCategoryNewsFragment2;
    private Bundle mBundle;
    private long mFirstTimeToBack;
    private boolean mFirstCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initHomeFragment();
        updateTheme();
    }

    private void initData() {
        mBundle = new Bundle();//传递Fragment参数
        mFragmentManager = getSupportFragmentManager();//Fragment管理器
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDlMenu, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();//抽屉按键的动画效果
        mDlMenu.addDrawerListener(toggle);
        initNavigation();
    }

    /**
     * 初始化抽屉内容
     */
    private void initNavigation() {
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
            mCategoryNewsFragment1.setArguments(mBundle);
        } else {
            mCategoryNewsFragment2.setArguments(mBundle);
        }

        mFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mFirstCategoryFragment
                        ? mCategoryNewsFragment1
                        : mCategoryNewsFragment2)
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
        mFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mHomeNewsFragment)
                .commit();
    }

    private void initView() {
        mHomeNewsFragment = new HomeNewsFragment();
        mCategoryNewsFragment1 = new CategoryNewsFragment();
        mCategoryNewsFragment2 = new CategoryNewsFragment();
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
            if (secondTime - mFirstTimeToBack > 2000) {
                Snackbar.make(mFlHomeContent, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                mFirstTimeToBack = secondTime;
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setIcon(isNightMode()
                ? R.drawable.ic_light_theme
                : R.drawable.ic_night_theme);
        menu.getItem(0).setTitle(isNightMode()
                ? R.string.light_mode
                : R.string.night_mode);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDlMenu.isDrawerOpen(GravityCompat.START)) {
            mDlMenu.closeDrawers();
        }
        switch (item.getItemId()) {
            case R.id.menu_item_day_or_night_mode:
                setNightMode(!isNightMode());
                item.setTitle(isNightMode()
                        ? R.string.light_mode
                        : R.string.night_mode);
                item.setIcon(isNightMode()
                        ? R.drawable.ic_light_theme
                        : R.drawable.ic_night_theme);
                updateTheme();
                break;
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

                        //清除cache文件夹以及Web缓存
                        File fileCache = new File(getCacheDir().getPath());
                        File webCache = new File(getFilesDir().getParent() + "/app_webview");
                        final boolean isSuccessful = deleteAllFile(fileCache)
                                && deleteAllFile(webCache);

                        UiUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mHomeNewsFragment.onRefresh();
                                Snackbar.make(mFlHomeContent,
                                        isSuccessful ? "清除缓存成功" : "清除缓存失败或无缓存",
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

    private void updateTheme() {
        mToolbar.setBackgroundResource(isNightMode()
                ? R.color.colorGrayBlack
                : R.color.colorPrimary);
        mDlMenu.setBackgroundResource(isNightMode()
                ? R.color.colorDarkGray
                : R.color.colorWhite);
        mNvDrawer.setBackgroundResource(isNightMode()
                ? R.color.colorGray
                : R.color.colorWhite);
        mNvDrawer.setItemBackgroundResource(isNightMode()
                ? R.drawable.menu_item_bg_night
                : R.drawable.menu_item_bg_light);
        mHomeNewsFragment.updateTheme();
        mCategoryNewsFragment1.updateTheme();
        mCategoryNewsFragment2.updateTheme();
        //TODO 夜间模式抽屉字体的颜色
//        mNvDrawer.setItemTextColor(new ColorStateList(new int[][]{}, new int[]{Color.WHITE}));
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
