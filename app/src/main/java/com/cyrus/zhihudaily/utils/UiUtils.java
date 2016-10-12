package com.cyrus.zhihudaily.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.cyrus.zhihudaily.BaseApplication;

/**
 * 界面UI工具类
 * <p>
 * Created by Cyrus on 2016/4/25.
 */
public class UiUtils {

    public static int getDimens(int height) {
        return (int) BaseApplication.getApplication().getResources().getDimension(height);
    }

    /**
     * 获得当前应用程序的上下文
     *
     * @return 应用程序的上下文
     */
    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    public static int setColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid() == BaseApplication.getMainTid()) {
            runnable.run();
        } else {
            BaseApplication.getHandler().post(runnable);
        }
    }

    public static void cancel(Runnable runnable) {
        BaseApplication.getHandler().removeCallbacks(runnable);
    }

    public static void postDelayed(Runnable autoRunTask, int time) {
        BaseApplication.getHandler().postDelayed(autoRunTask, time);
    }

}
