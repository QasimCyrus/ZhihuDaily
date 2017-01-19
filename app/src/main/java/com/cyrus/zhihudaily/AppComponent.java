package com.cyrus.zhihudaily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.cyrus.zhihudaily.base.BaseActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * 管理全局类实例的Component
 * <p>
 * Created by Cyrus on 2017/1/14.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Context getContext();

    SharedPreferences getPreference();

    Handler getHandler();

    int getTid();

    NetApi getNetApi();

    OkHttpClient getOkHttpClient();

    Picasso getPicasso();

    void inject(BaseActivity baseActivity);

}
