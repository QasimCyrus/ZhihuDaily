package com.cyrus.zhihudaily.base;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.response.BaseResponse;
import com.cyrus.zhihudaily.response.ErrorCode;
import com.cyrus.zhihudaily.utils.ResponseUtil;
import com.cyrus.zhihudaily.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 覆写CallBack加载失败时的方法，减少重复代码
 *
 * <p>
 * Created by Cyrus on 2017/1/11.
 */

public abstract class BaseCallback<T extends BaseResponse> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (ResponseUtil.isNormal(response.body())) {
            onResponseNormal(call, response);
        } else {
            onResponseAbnormal(call, response);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        ToastUtil.show("请检查您的网络设置");
    }

    public abstract void onResponseNormal(Call<T> call, Response<T> response);

    public void onResponseAbnormal(Call<T> call, Response<T> response) {
        if (response == null) {
            ToastUtil.show(R.string.ser_error);
        } else {
            ToastUtil.show(ErrorCode.getErrorMsg(response.body().getCode()));
        }
    }

}
