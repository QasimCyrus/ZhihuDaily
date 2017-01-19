package com.cyrus.zhihudaily.splash;

import android.content.SharedPreferences;
import android.os.Handler;

import com.cyrus.zhihudaily.NetApi;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.base.BaseCallback;
import com.cyrus.zhihudaily.constants.PreferenceConstant;
import com.cyrus.zhihudaily.response.SplashResponse;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 启动页的Presenter实现类
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
final class SplashPresenter implements SplashContract.Presenter {

    private static final int FIRST_LOAD_TIME = 3000;
    private static final int OTHER_LOAD_TIME = 1500;

    private SplashContract.View mSplashView;
    private SharedPreferences mPreferences;
    private Handler mHandler;
    private NetApi mNetApi;
    private Picasso mPicasso;

    @Inject
    SplashPresenter(SplashContract.View splashView, Handler handler,
                    SharedPreferences preferences, NetApi netApi, Picasso picasso) {
        mSplashView = splashView;
        mPreferences = preferences;
        mHandler = handler;
        mNetApi = netApi;
        mPicasso = picasso;
    }

    @Inject
    void setupListeners() {
        mSplashView.setPresenter(this);
    }

    @Override
    public void start() {
        boolean isFirstLaunch = mPreferences.getBoolean(PreferenceConstant.IS_FIRST_LAUNCH, true);
        if (isFirstLaunch) {
            mNetApi.getSplashImage().enqueue(new BaseCallback<SplashResponse>() {
                @Override
                public void onResponseNormal(Call<SplashResponse> call,
                                             Response<SplashResponse> response) {
                    mPicasso.load(response.body().getImg()).into(mSplashView.getSplashView());
                    mSplashView.setSplashText(response.body().getText());
                    mPreferences.edit().putBoolean(PreferenceConstant.IS_FIRST_LAUNCH, false).apply();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jump2MainActivity();
                        }
                    }, FIRST_LOAD_TIME);
                }
            });
        } else {
            mPicasso.load(R.drawable.splash).into(mSplashView.getSplashView());
            mSplashView.setSplashText(UiUtils.getString(R.string.app_name));
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    jump2MainActivity();
                }
            }, OTHER_LOAD_TIME);
        }
    }

    private void jump2MainActivity() {
        mSplashView.jump2MainActivity();
    }

}
