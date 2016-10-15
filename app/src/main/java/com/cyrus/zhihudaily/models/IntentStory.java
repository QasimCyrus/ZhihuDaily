package com.cyrus.zhihudaily.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用于活动间传递的新闻结构体
 * <p>
 * Created by Cyrus on 2016/10/15.
 */
public class IntentStory implements Serializable {

    @SerializedName("id")
    private String mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("images")
    private ArrayList<String> mImages;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }
}
