package com.cyrus.zhihudaily.utils;

import com.cyrus.zhihudaily.AppComponent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * 在Dagger中，没有使用注解的Component不能依赖于使用了注解的Component，
 * 因为{@link AppComponent}使用了@Singleton注解，
 * 因此我们为所有Fragment Components创建一个注解。
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentScope {
}
