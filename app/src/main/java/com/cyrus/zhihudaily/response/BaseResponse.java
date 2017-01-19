package com.cyrus.zhihudaily.response;


import com.google.gson.annotations.Expose;

/**
 * Retrofit响应体基类
 * <p>
 * Created by Cyrus on 17/1/17.
 */
public class BaseResponse {

    @Expose
    private String msg;
    @Expose
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
