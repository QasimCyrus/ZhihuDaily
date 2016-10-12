package com.cyrus.zhihudaily;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;

import static com.cyrus.zhihudaily.constants.SharePreferenceConstant.PREFERENCE_NAME;

/**
 * Created by Cyrus on 2016/10/9.
 */

public class BaseApplication extends Application {

    private static BaseApplication sApplication;
    private static int sMainTid;
    private static Handler sHandler;
    private SharedPreferences mSp;

    @Override
    public void onCreate() {
        super.onCreate();

        mSp = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        sApplication = this;
        sMainTid = android.os.Process.myTid();
        sHandler = new Handler();
    }

    public SharedPreferences getSp() {
        return mSp;
    }

    public static int getMainTid() {
        return sMainTid;
    }

    public static Handler getHandler() {
        return sHandler;
    }

    public static BaseApplication getApplication(){
        return sApplication;
    }
}
