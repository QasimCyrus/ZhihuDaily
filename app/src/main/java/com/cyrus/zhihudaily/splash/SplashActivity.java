package com.cyrus.zhihudaily.splash;

import android.os.Bundle;

import com.cyrus.zhihudaily.App;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.base.BaseActivity;
import com.cyrus.zhihudaily.utils.ActivityUtils;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {

    @Inject
    SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashFragment fragment = (SplashFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = SplashFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.fragment_container);
        }

        DaggerSplashComponent.builder()
                .appComponent(App.getAppComponent())
                .splashPresenterModule(new SplashPresenterModule(fragment))
                .build().inject(this);
    }

}
