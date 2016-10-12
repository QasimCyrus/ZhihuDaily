package com.cyrus.zhihudaily.utils;

import android.util.Log;
import android.widget.ImageView;

import com.cyrus.zhihudaily.R;
import com.squareup.picasso.Picasso;

/**
 * 使用Picasso加载图片的封装
 * <p>
 * Created by Cyrus on 2016/5/8.
 */
public class LoadImageUtils {

    public static void loadImage(String url, ImageView imageView) {
        if ("default".equals(url)) {
            Log.d("加载图片是", url);
            Picasso.with(UiUtils.getContext()).load(R.drawable.splash).into(imageView);
        }
        Picasso.with(UiUtils.getContext()).load(url).into(imageView);
    }

}
