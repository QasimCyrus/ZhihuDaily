package com.cyrus.zhihudaily.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 消息标题的数据类型
 * <p>
 * Created by Cyrus on 2016/5/2.
 */
public class Story implements Serializable {

    @SerializedName("images")
    private ArrayList<String> mImages;
    @SerializedName("ga_prefix")
    private String mGaPrefix;
    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("type")
    private String mType;
    @SerializedName("date")
    private String mDate;//用来记录日期

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getGaPrefix() {
        return mGaPrefix;
    }

    public void setGaPrefix(String gaPrefix) {
        this.mGaPrefix = gaPrefix;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        this.mImages = images;
    }

    @Override
    public String toString() {
        return "DailyNewsStoryData{" +
                "mGaPrefix='" + mGaPrefix + '\'' +
                ", mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mType='" + mType + '\'' +
                ", mImages=" + mImages +
                '}';
    }

}
