package com.cyrus.zhihudaily.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 分类新闻的结构体
 * <p>
 * Created by Cyrus on 2016/10/22.
 */

public class CategoryNewsData implements Serializable {
    @SerializedName("stories")
    private List<Story> mStories;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("background")
    private String mBackground;
    @SerializedName("color")
    private String mColor;
    @SerializedName("name")
    private String mName;
    @SerializedName("image")
    private String mImage;
    @SerializedName("editors")
    private List<Story> mEditors;
    @SerializedName("image_source")
    private String mImageSource;
    @SerializedName("theme_id")
    private String mThemeId;

    public String getThemeId() {
        return mThemeId;
    }

    public void setThemeId(String themeId) {
        mThemeId = themeId;
    }

    public List<Story> getStories() {
        return mStories;
    }

    public void setStories(List<Story> stories) {
        mStories = stories;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getBackground() {
        return mBackground;
    }

    public void setBackground(String background) {
        mBackground = background;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public List<Story> getEditors() {
        return mEditors;
    }

    public void setEditors(List<Story> editors) {
        mEditors = editors;
    }

    public String getImageSource() {
        return mImageSource;
    }

    public void setImageSource(String imageSource) {
        mImageSource = imageSource;
    }
}
