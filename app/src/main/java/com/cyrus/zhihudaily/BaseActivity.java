package com.cyrus.zhihudaily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 封装参数的基本活动类
 * <p>
 * Created by Cyrus on 2016/10/9.
 */

public class BaseActivity extends AppCompatActivity {
    private BaseApplication mBaseApplication;
    protected String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseApplication = (BaseApplication) getApplication();
        TAG = getClass().getPackage().getName();
    }

    public BaseApplication getBaseApplication() {
        return mBaseApplication;
    }
}
