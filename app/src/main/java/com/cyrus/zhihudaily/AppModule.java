package com.cyrus.zhihudaily;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 提供全局类实例的Module
 * <p>
 * Created by Cyrus on 2017/1/14.
 */
@Module
public class AppModule {

    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10M

    private final App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    Context providesAppContext() {
        return mApp;
    }

    @Provides
    @Singleton
    SharedPreferences providesAppPreference() {
        return mApp.getPreferences();
    }

    @Provides
    @Singleton
    Handler providesAppHandler() {
        return mApp.getHandler();
    }

    @Provides
    @Singleton
    int providesAppTid() {
        return mApp.getTid();
    }

    @Singleton
    @Provides
    NetApi providesNetApi(OkHttpClient client) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(NetApi.BASE_URL)
                .build()
                .create(NetApi.class);
    }

    @Singleton
    @Provides
    OkHttpClient providesOkHttpClient() {
        File cacheDir = new File(mApp.getExternalCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(getLoggingInterceptor())
                .build();
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("retrofit", "retrofit:" + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Singleton
    @Provides
    Picasso providesPicasso(OkHttpClient client) {
        return new Picasso.Builder(mApp)
                .downloader(new OkHttp3Downloader(client))
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri,
                                                  Exception exception) {
                        Log.e("Picasso", "Load fail: " + uri.toString());
                    }
                })
                .build();
    }

}
