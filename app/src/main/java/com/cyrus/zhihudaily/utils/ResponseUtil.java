package com.cyrus.zhihudaily.utils;

import com.cyrus.zhihudaily.response.BaseResponse;

/**
 * 判断响应体是否正常的工具类
 * <p>
 * Created by Cyrus on 2017/1/10.
 */

public class ResponseUtil {

    public static boolean isNormal(BaseResponse response) {
        return response != null && response.getCode() == 0;
    }

}
