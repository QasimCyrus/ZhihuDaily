package com.cyrus.zhihudaily.models;

import java.io.Serializable;

/**
 * 头条消息的数据类型
 * <p>
 * Created by Cyrus on 2016/5/2.
 */
public class TopStory implements Serializable {
    private String ga_prefix;
    private String id;
    private String title;
    private String type;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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


}
