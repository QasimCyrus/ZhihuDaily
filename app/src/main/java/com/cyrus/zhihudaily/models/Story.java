package com.cyrus.zhihudaily.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 消息标题的数据类型
 * <p>
 * Created by Curus on 2016/5/2.
 */
public class Story implements Serializable {
    public ArrayList<String> images;
    private String ga_prefix;
    private String id;
    private String title;
    private String type;
    private String date;//用来记录日期

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "DailyNewsStoryData{" +
                "ga_prefix='" + ga_prefix + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", images=" + images +
                '}';
    }


}
