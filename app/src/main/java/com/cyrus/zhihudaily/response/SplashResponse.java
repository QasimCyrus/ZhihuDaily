package com.cyrus.zhihudaily.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 启动页数据响应体
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
public class SplashResponse extends BaseResponse {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("img")
    @Expose
    private String img;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
