package com.cyrus.zhihudaily.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 头条消息的数据类型
 * <p>
 * Created by Cyrus on 2016/5/2.
 */
public class TopStory implements Serializable {

    @SerializedName("ga_prefix")
    private String mGaPrefix;
    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("type")
    private String mType;
    @SerializedName("image")
    private String mImage;

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
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


}
