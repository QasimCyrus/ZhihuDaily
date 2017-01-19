package com.cyrus.zhihudaily;

import com.cyrus.zhihudaily.response.SplashResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 知乎日报的API接口
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
public interface NetApi {

    String BASE_URL = "http://news-at.zhihu.com/api/4/";

    @GET("start-image/720*1280")
    Call<SplashResponse> getSplashImage();

//    @GET("news/latest")

}
