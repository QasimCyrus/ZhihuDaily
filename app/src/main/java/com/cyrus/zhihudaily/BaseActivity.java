package com.cyrus.zhihudaily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Cyrus on 2016/10/9.
 */

public class BaseActivity extends AppCompatActivity {
    private BaseApplication mBaseApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseApplication = (BaseApplication) getApplication();
    }

    public BaseApplication getBaseApplication() {
        return mBaseApplication;
    }
}
