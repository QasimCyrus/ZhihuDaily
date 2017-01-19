package com.cyrus.zhihudaily.response;

import android.util.SparseArray;

/**
 * 错误码集合
 * <p>
 * Created by Cyrus on 2017/1/10.
 */

public class ErrorCode {

    private static SparseArray<String> mErrorCode;

    static {
        if (mErrorCode == null) {
            synchronized (ErrorCode.class) {
                if (mErrorCode == null) {
                    mErrorCode = new SparseArray<>();
                    mErrorCode.put(0, "请求正常");
                    mErrorCode.put(1000, "服务器异常");
                    mErrorCode.put(1001, "请求接口缺少必要参数");
                    mErrorCode.put(1003, "请求接口参数类型错误");
                    mErrorCode.put(1010, "服务器资源超限，请稍后重试");
                }
            }
        }
    }

    public static String getErrorMsg(int code) {
        return mErrorCode.get(code);
    }

}
