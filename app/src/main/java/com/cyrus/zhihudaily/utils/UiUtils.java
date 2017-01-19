package com.cyrus.zhihudaily.utils;

import android.support.v4.content.ContextCompat;

import com.cyrus.zhihudaily.App;
import com.cyrus.zhihudaily.AppComponent;

/**
 * 界面UI工具类
 * <p>
 * Created by Cyrus on 2016/4/25.
 */
public class UiUtils {

    private static AppComponent sAppComponent = App.getAppComponent();

    public static int getDimens(int height) {
        return (int) App.getApp().getResources().getDimension(height);
    }

    /**
     * 获得颜色
     *
     * @param id 颜色的资源ID
     * @return 返回一个颜色的值
     */
    public static int getColor(int id) {
        return ContextCompat.getColor(App.getApp(), id);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = App.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getString(int resourceId) {
        return App.getApp().getResources().getString(resourceId);
    }

    /**
     * 在主线程（UI线程）里执行任务
     *
     * @param runnable 要执行的任务
     */
    public static void runOnUiThread(Runnable runnable) {
        if (android.os.Process.myTid() == sAppComponent.getTid()) {//如果当前线程ID是主线程ID
            runnable.run();//执行runnable
        } else {//如果当前ID不是主线程ID，将runnable放入队列中等待执行
            sAppComponent.getHandler().post(runnable);
        }
    }

    /**
     * 取消某个任务
     *
     * @param runnable 要取消的任务
     */
    public static void cancel(Runnable runnable) {
        sAppComponent.getHandler().removeCallbacks(runnable);
    }

    /**
     * 延迟执行任务
     *
     * @param autoRunTask 要执行的任务
     * @param time        延迟的时间（毫秒）
     */
    public static void postDelayed(Runnable autoRunTask, int time) {
        sAppComponent.getHandler().postDelayed(autoRunTask, time);
    }

}
