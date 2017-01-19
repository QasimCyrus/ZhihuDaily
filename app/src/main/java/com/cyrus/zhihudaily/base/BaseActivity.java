package com.cyrus.zhihudaily.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cyrus.zhihudaily.App;
import com.cyrus.zhihudaily.constants.PreferenceConstant;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * 封装参数的Activity基类
 * <p>
 * Created by Cyrus on 2016/10/9.
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * 活动的标志
     */
    protected String TAG;
    /**
     * 程序上下文
     */
    @Inject
    SharedPreferences mPreferences;
    /**
     * 夜间模式为true，日间模式为false
     */
    private boolean mIsNightMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);

        mIsNightMode = mPreferences.getBoolean(PreferenceConstant.IS_NIGHT_MODE, false);
        TAG = getClass().getPackage().getName();
    }

    public boolean isNightMode() {
        return mIsNightMode;
    }

    public void setNightMode(boolean nightMode) {
        mIsNightMode = nightMode;
        mPreferences.edit()
                .putBoolean(PreferenceConstant.IS_NIGHT_MODE, mIsNightMode)
                .apply();
    }
}
