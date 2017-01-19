package com.cyrus.zhihudaily.base;

/**
 * MVP：View基类
 * <p>
 * Created by Cyrus on 2017/1/17.
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

}
