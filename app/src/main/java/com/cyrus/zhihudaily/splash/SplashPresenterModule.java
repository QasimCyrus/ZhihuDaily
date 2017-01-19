package com.cyrus.zhihudaily.splash;

import dagger.Module;
import dagger.Provides;

/**
 * 启动页面的Module
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
@Module
public class SplashPresenterModule {

    private final SplashContract.View mSplashView;

    public SplashPresenterModule(SplashContract.View splashView) {
        mSplashView = splashView;
    }

    @Provides
    SplashContract.View providesSplashView() {
        return mSplashView;
    }

}
