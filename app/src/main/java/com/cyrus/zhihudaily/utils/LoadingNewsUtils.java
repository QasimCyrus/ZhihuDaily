package com.cyrus.zhihudaily.utils;

import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.database.NewsDB;
import com.cyrus.zhihudaily.models.NewsData;
import com.cyrus.zhihudaily.models.Story;
import com.google.gson.Gson;

/**
 * 加载新闻的工具类
 * <p>
 * Created by Cyrus on 2016/10/12.
 */

public class LoadingNewsUtils {

    private static String sDate;
    private boolean mState;

    public static NewsData load(String url) {
        NewsDB newsDB = new NewsDB();
        if (url.contains("latest")) {//要获取最新的数据
            String date = DateUtils.getUserDate("yyyyMMdd");
            sDate = date;
            String json = NetUtils.load(url);

            //查找是否存在今天的数据
            if (newsDB.isTodayDataExist(date)) {//存在今天的数据，则获得原来的数据内容
                String before = newsDB.find(date);
                if (json == null) {
                    if (before == null) {
                        return null;
                    } else {
                        return parse(before);
                    }
                } else {
                    if (json.equals(before)) {//加载的数据和原来的相同，则返回原来的数据
                        return parse(before);
                    } else {//加载的数据和原来不同，则返回加载到的数据
                        newsDB.update(date, json);
                        return parse(json);
                    }
                }
            } else {//不存在今天的数据
                if (json == null) {
                    return null;
                } else {
                    newsDB.insert(date, json);
                    return parse(json);
                }
            }
        } else if (url.contains("before")) {//要获取以往的数据
            String date = url.replace(GlobalConstant.BEFORE_NEWS_URL, "");
            sDate = date;
            if (newsDB.isTodayDataExist(date)) {//数据库中有以前的数据
                String json = newsDB.find(date);
                return parse(json);
            } else {//数据库中没有数据，从网络加载
                String json = NetUtils.load(url);
                if (json == null) {
                    return null;
                } else {
                    newsDB.insert(date, json);
                    return parse(json);
                }
            }
        } else {
            return null;
        }

    }

    private static NewsData parse(String result) {
        Gson gson = new Gson();
        NewsData newsData = gson.fromJson(result, NewsData.class);

        for (Story story : newsData.getStories()) {
            story.setDate(sDate);
        }

        return newsData;
    }

    public boolean isState() {
        return mState;
    }

    public void setState(boolean state) {
        mState = state;
    }

}
