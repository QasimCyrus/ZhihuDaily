package com.cyrus.zhihudaily.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.App;
import com.cyrus.zhihudaily.base.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.PreferenceConstant;
import com.cyrus.zhihudaily.database.NewsDB;
import com.cyrus.zhihudaily.database.NewsDetailDB;
import com.cyrus.zhihudaily.database.ThemeNewsDB;
import com.cyrus.zhihudaily.fragment.CategoryNewsFragment;
import com.cyrus.zhihudaily.fragment.DateNewsFragment;
import com.cyrus.zhihudaily.fragment.HomeNewsFragment;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.utils.UiUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDlMenu;
    private Toolbar mToolbar;
    private NavigationView mNvDrawer;
    private FrameLayout mFlHomeContent;
    private HomeNewsFragment mHomeNewsFragment;
    private CategoryNewsFragment mCategoryNewsFragment;
    private DateNewsFragment mDateNewsFragment;
    private long mFirstPressBackTime;
    private FragmentManager mFragmentManager;

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
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDlMenu, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();//抽屉按键的动画效果
        mDlMenu.addDrawerListener(toggle);
        mFragmentManager = getSupportFragmentManager();
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
                        setCategoryFragment(ThemeId.COMIC_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_company:
                        setCategoryFragment(ThemeId.COMPANY_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_design:
                        setCategoryFragment(ThemeId.DESIGN_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_finance:
                        setCategoryFragment(ThemeId.FINANCE_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_game:
                        setCategoryFragment(ThemeId.GAME_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_internet:
                        setCategoryFragment(ThemeId.INTERNET_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_movie:
                        setCategoryFragment(ThemeId.MOVIE_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_music:
                        setCategoryFragment(ThemeId.MUSIC_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_not_boring:
                        setCategoryFragment(ThemeId.NOT_BORING_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_psychology:
                        setCategoryFragment(ThemeId.PSYCHOLOGY_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_recommend:
                        setCategoryFragment(ThemeId.RECOMMEND_ID, item.getTitle().toString());
                        return true;
                    case R.id.nav_sport:
                        setCategoryFragment(ThemeId.SPORT_ID, item.getTitle().toString());
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 设置分类新闻的界面
     *
     * @param id    分类新闻的主题
     * @param title 分类新闻界面的标题
     */
    private void setCategoryFragment(String id, String title) {
        mToolbar.setTitle(title);
        mCategoryNewsFragment.setThemeId(id);

        mFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mCategoryNewsFragment)
                .commit();
    }

    /**
     * 设置某个日期的新闻的界面
     *
     * @param date  要查看新闻的日期
     * @param title 某个日期的新闻界面的标题
     */
    private void setDateNewsFragment(String date, String title) {
        mToolbar.setTitle(title);
        mDateNewsFragment.setDate(date);

        mFragmentManager.beginTransaction()
                .replace(R.id.fl_content, mDateNewsFragment)
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
        mCategoryNewsFragment = new CategoryNewsFragment();
        mDateNewsFragment = new DateNewsFragment();
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
            if (secondTime - mFirstPressBackTime > 2000) {
                Snackbar.make(mFlHomeContent, "再按一次退出", Snackbar.LENGTH_SHORT).show();
                mFirstPressBackTime = secondTime;
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(1).setIcon(isNightMode()
                ? R.drawable.ic_light_theme
                : R.drawable.ic_night_theme);
        menu.getItem(1).setTitle(isNightMode()
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
            case R.id.menu_item_pick_date://选择新闻日期
                //知乎日报最早时间：2015.05.20
                Calendar minCalendar = Calendar.getInstance();
                minCalendar.set(2013, 4, 20);
                Date minDate = minCalendar.getTime();

                Calendar maxCalendar = Calendar.getInstance();
                Date maxDate = maxCalendar.getTime();

                View view = View.inflate(this, R.layout.item_date_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                datePicker.setCalendarViewShown(false);
                datePicker.setMinDate(minDate.getTime());
                datePicker.setMaxDate(maxDate.getTime());

                new AlertDialog.Builder(this)
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获得选择的日期
                                String yyyy = String.valueOf(datePicker.getYear());

                                int month = datePicker.getMonth();
                                String MM = String.valueOf(month + 1);
                                if (month < 9) {
                                    MM = "0" + MM;
                                }

                                int day = datePicker.getDayOfMonth();
                                String dd = String.valueOf(day);
                                if (day < 10) {
                                    dd = "0" + dd;
                                }

                                String pickDate = yyyy + MM + dd;
                                String title = yyyy + "年" + MM + "月" + dd + "日";

                                setDateNewsFragment(pickDate, title);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            case R.id.menu_item_day_or_night_mode:// 切换夜间模式
                setNightMode(!isNightMode());
                item.setTitle(isNightMode()
                        ? R.string.light_mode
                        : R.string.night_mode);
                item.setIcon(isNightMode()
                        ? R.drawable.ic_light_theme
                        : R.drawable.ic_night_theme);
                updateTheme();
                break;
            case R.id.menu_item_clear_cache:// 清除缓存
                ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        //清除SharePreference缓存
                        boolean isNightMode = isNightMode();
                        App.getAppComponent().getPreference().edit()
                                .clear()
                                .putBoolean(PreferenceConstant.IS_NIGHT_MODE, isNightMode)
                                .apply();
                        getSharedPreferences(PreferenceConstant
                                .NEWS_PREFERENCE_NAME, MODE_PRIVATE).edit()
                                .clear()
                                .apply();

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
                                        isSuccessful ? "清除缓存成功" : "清除缓存失败或无网页缓存",
                                        Snackbar.LENGTH_SHORT).show();
                                updateTheme();
                            }
                        });
                    }
                });
                return true;
            case R.id.menu_item_about://关于
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
        mCategoryNewsFragment.updateTheme();
        mDateNewsFragment.updateTheme();
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
