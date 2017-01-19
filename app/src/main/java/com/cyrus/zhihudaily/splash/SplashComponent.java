package com.cyrus.zhihudaily.splash;

import com.cyrus.zhihudaily.AppComponent;
import com.cyrus.zhihudaily.utils.FragmentScope;

import dagger.Component;

/**
 * 启动页的Component；
 * 依赖于注解了@Singleton的{@link AppComponent}，因此需要加上@FragmentScope注解。
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = SplashPresenterModule.class)
public interface SplashComponent {

    void inject(SplashActivity splashActivity);

}
