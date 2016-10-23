package com.cyrus.zhihudaily.utils;

import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.database.NewsDB;
import com.cyrus.zhihudaily.database.ThemeNewsDB;
import com.cyrus.zhihudaily.models.CategoryNewsData;
import com.cyrus.zhihudaily.models.LatestNewsData;
import com.cyrus.zhihudaily.models.Story;
import com.google.gson.Gson;

/**
 * 加载新闻的工具类
 * <p>
 * Created by Cyrus on 2016/10/12.
 */

public class LoadingNewsUtils {

    /**
     * 新闻时间
     */
    private static String sDate;
    /**
     * 新闻主题
     */
    private static String sTheme;

    /**
     * 加载最新的新闻
     *
     * @param url 最新新闻的url
     * @return 最新的新闻结构体
     */
    public static LatestNewsData loadLatest(String url) {
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
                        return parseLatest(before);
                    }
                } else {
                    if (json.equals(before)) {//加载的数据和原来的相同，则返回原来的数据
                        return parseLatest(before);
                    } else {//加载的数据和原来不同，则返回加载到的数据
                        newsDB.update(date, json);
                        return parseLatest(json);
                    }
                }
            } else {//不存在今天的数据
                if (json == null) {
                    return null;
                } else {
                    newsDB.insert(date, json);
                    return parseLatest(json);
                }
            }
        } else if (url.contains("before")) {//要获取以往的数据
            String date = url.replace(GlobalConstant.BEFORE_NEWS_URL, "");
            sDate = date;
            if (newsDB.isTodayDataExist(date)) {//数据库中有以前的数据
                String json = newsDB.find(date);
                return parseLatest(json);
            } else {//数据库中没有数据，从网络加载
                String json = NetUtils.load(url);
                if (json == null) {
                    return null;
                } else {
                    newsDB.insert(date, json);
                    return parseLatest(json);
                }
            }
        } else {
            return null;
        }

    }

    /**
     * 加载分类新闻
     *
     * @param url 要加载新闻的url
     * @return 加载到的分类新闻结构体
     */
    public static CategoryNewsData loadCategory(String url) {
        ThemeNewsDB newsDB = new ThemeNewsDB();
        sTheme = url.substring(url.lastIndexOf("/") + 1);
        String json = NetUtils.load(url);
        if (newsDB.isThemeNewsExist(sTheme)) {
            String oldData = newsDB.find(sTheme);
            if (json == null) {
                if (oldData == null) {
                    return null;
                } else {
                    return parseCategory(oldData);
                }
            } else {
                if (json.equals(oldData)) {
                    return parseCategory(oldData);
                } else {
                    newsDB.update(sTheme, json);
                    return parseCategory(json);
                }
            }
        } else {
            if (json == null) {
                return null;
            } else {
                newsDB.insert(sTheme, json);
                return parseCategory(json);
            }
        }
    }

    /**
     * 把分类新闻的json字符串转换成分类新闻的数据结构
     *
     * @param json 要转换的分类新闻的json字符串
     * @return 分类新闻的数据结构
     */
    private static CategoryNewsData parseCategory(String json) {
        Gson gson = new Gson();
        CategoryNewsData newsData = gson.fromJson(json, CategoryNewsData.class);
        newsData.setThemeId(sTheme);
        return newsData;
    }

    /**
     * 把最新的新闻json字符串转换成最新新闻的数据结构
     *
     * @param result 要转换的最新新闻json字符串
     * @return 最新的新闻的数据结构
     */
    private static LatestNewsData parseLatest(String result) {
        Gson gson = new Gson();
        LatestNewsData newsData = gson.fromJson(result, LatestNewsData.class);

        for (Story story : newsData.getStories()) {
            story.setDate(sDate);
        }

        return newsData;
    }

}
