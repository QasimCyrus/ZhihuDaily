package com.cyrus.zhihudaily.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.cyrus.zhihudaily.App;


/**
 * 吐司工具类
 * <p>
 * Created by Cyrus on 2017/1/10.
 */

public class ToastUtil {

    private static Toast sToast;
    private static Snackbar sSnackBar;

    public static void show(View view, String msg) {
        if (sSnackBar == null) {
            sSnackBar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        } else {
            sSnackBar.setText(msg);
        }
        sSnackBar.show();
    }

    public static void show(View view, int resId) {
        if (sSnackBar == null) {
            sSnackBar = Snackbar.make(view, resId, Snackbar.LENGTH_SHORT);
        } else {
            sSnackBar.setText(resId);
        }
        sSnackBar.show();
    }

    public static void show(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(App.getAppComponent().getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void show(int resId) {
        if (sToast == null) {
            sToast = Toast.makeText(App.getAppComponent().getContext(), resId, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(resId);
        }
        sToast.show();
    }
}
