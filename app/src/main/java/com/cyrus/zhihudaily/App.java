package com.cyrus.zhihudaily;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import static com.cyrus.zhihudaily.constants.PreferenceConstant.PREFERENCE_NAME;

/**
 * 管理整个程序的类，生命周期是与整个程序相同。
 * 引用着{@link AppComponent}只初始化一次
 * <p>
 * Created by Cyrus on 2016/10/9.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "Lifecycle";

    private static App sApp;
    private static AppComponent sAppComponent;
    private int mTid;
    private Handler mHandler;
    private SharedPreferences mPreferences;

    public static App getApp() {
        return sApp;
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;
        registerActivityLifecycleCallbacks(this);

        mTid = android.os.Process.myTid();
        mHandler = new Handler();
        mPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public int getTid() {
        return mTid;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onCreate");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onSaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG, activity.getClass().getSimpleName() + " onDestroyed");
    }
}
