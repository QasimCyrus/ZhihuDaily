package com.cyrus.zhihudaily.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.SplashInfo;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
import com.cyrus.zhihudaily.utils.NetUtils;
import com.google.gson.Gson;

import static com.cyrus.zhihudaily.constants.SharePreferenceConstant.FIRST_LOAD;

public class SplashActivity extends BaseActivity {

    private ImageView mIvSplash;
    private TextView mTvTitle;
    private SharedPreferences mSp;

    private boolean mIsFirstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSp = getBaseApplication().getSp();
        mIsFirstLoad = mSp.getBoolean(FIRST_LOAD, true);

        initView();
        getData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 3000);
    }

    private void getData() {
        if (mIsFirstLoad) {
            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    final String strSplashUrl = NetUtils.load(GlobalConstant.SPLASH_URL);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData(strSplashUrl);
                        }
                    });
                }
            });

            mIsFirstLoad = false;
            mSp.edit().putBoolean(FIRST_LOAD, mIsFirstLoad).apply();
        } else {
            setDefaultView();
        }
    }

    private void setData(String data) {
        if (data != null) {
            SplashInfo splashInfo = new Gson().fromJson(data, SplashInfo.class);
            LoadImageUtils.loadImage(splashInfo.getImg(), mIvSplash);
            mTvTitle.setText(splashInfo.getText());
        } else {
            setDefaultView();
        }
    }

    private void setDefaultView() {
        mIvSplash.setImageResource(R.drawable.start);
        mTvTitle.setText(R.string.app_name);
    }

    private void initView() {
        mIvSplash = (ImageView) findViewById(R.id.iv_splash);
        mTvTitle = (TextView) findViewById(R.id.tv_splash);
    }
}
