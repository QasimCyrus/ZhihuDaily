package com.cyrus.zhihudaily.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络工具类
 * Created by Cyrus on 2016/5/8.
 */
public class NetUtils {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    /**
     * 加载URL，返回String类型
     *
     * @param url 要解析的url
     * @return 解析到的String类型数据
     */
    public static String load(String url) {
        if (isNetConnectedOrConnecting()) {
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = mOkHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    response.close();
                    return result;
                } else {
                    response.close();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 判断当前网络是否已连接
     *
     * @return 已连接返回true，未连接或正在连接返回false
     */
    public static boolean isNetConnectedOrConnecting() {
        ConnectivityManager manager = (ConnectivityManager) UiUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    /**
     * 判断当前是否为wifi连接
     *
     * @return 如果当前是wifi连接，则返回true
     */
    public static boolean isWifi() {
        ConnectivityManager manager = (ConnectivityManager) UiUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

}
