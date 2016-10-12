package com.cyrus.zhihudaily.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 加载url的工具类
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
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
