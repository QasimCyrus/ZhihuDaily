package com.cyrus.zhihudaily.utils;

import android.os.Environment;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 图片的工具类
 * <p>
 * Created by Cyrus on 2016/5/8.
 */
public class ImageUtils {

    private static OkHttpClient sOkHttpClient = new OkHttpClient();

    /**
     * 使用Picasso加载图片
     *
     * @param url       图片url
     * @param imageView 要加载图片的imageView
     */
    public static void loadImage(String url, ImageView imageView) {
        if (url != null) {
            Picasso.with(UiUtils.getContext()).load(url).into(imageView);
        }
    }

    /**
     * 保存图片
     *
     * @param url 图片URL
     * @return 保存成功返回true，保存失败返回false
     */
    public static boolean saveImage(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = sOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream inputStream = response.body().byteStream();

                FileOutputStream fileOutputStream;
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    String path = Environment.getExternalStorageDirectory().getPath() + "/Pictures";
                    String fileName = url.substring(url.lastIndexOf("/"));
                    File file = new File(path, fileName);
                    fileOutputStream = new FileOutputStream(file);
                } else {
                    return false;
                }

                byte buf[] = new byte[8 * 1024];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }

                response.close();
                fileOutputStream.close();
                inputStream.close();

                return true;
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

}
