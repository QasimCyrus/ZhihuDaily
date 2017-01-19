package com.cyrus.zhihudaily.splash;

import android.widget.ImageView;

import com.cyrus.zhihudaily.base.BasePresenter;
import com.cyrus.zhihudaily.base.BaseView;

/**
 * 启动页MVP协议
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
public interface SplashContract {

    interface View extends BaseView<Presenter> {
        ImageView getSplashView();
        void setSplashText(String splashText);
        void jump2MainActivity();
    }

    interface Presenter extends BasePresenter {
    }

}
