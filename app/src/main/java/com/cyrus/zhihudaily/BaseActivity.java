package com.cyrus.zhihudaily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cyrus.zhihudaily.constants.SharePreferenceConstant;

/**
 * 封装参数的基本活动类
 * <p>
 * Created by Cyrus on 2016/10/9.
 */

public class BaseActivity extends AppCompatActivity {
    /**
     * 活动的标志
     */
    protected String TAG;
    /**
     * 夜间模式为true，日间模式为false
     */
    private boolean mIsNightMode;
    /**
     * 程序上下文
     */
    private BaseApplication mBaseApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseApplication = (BaseApplication) getApplication();
        mIsNightMode = mBaseApplication.getSp()
                .getBoolean(SharePreferenceConstant.IS_NIGHT_MODE, false);
        TAG = getClass().getPackage().getName();
    }

    public BaseApplication getBaseApplication() {
        return mBaseApplication;
    }

    public boolean isNightMode() {
        return mIsNightMode;
    }

    public void setNightMode(boolean nightMode) {
        mIsNightMode = nightMode;
        mBaseApplication.getSp().edit().putBoolean(
                SharePreferenceConstant.IS_NIGHT_MODE, mIsNightMode).apply();
    }
}
