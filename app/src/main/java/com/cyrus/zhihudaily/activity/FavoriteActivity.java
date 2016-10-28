package com.cyrus.zhihudaily.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.adapter.IntentStoryAdapter;
import com.cyrus.zhihudaily.database.FavoriteNewsDB;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FavoriteActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mRvFavorites;
    private TextView mTvTip;
    private IntentStoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        initToolbar();
        updateTheme();
    }

    private void updateTheme() {
        mToolbar.setBackgroundResource(isNightMode()
                ? R.color.colorGrayBlack
                : R.color.colorPrimary);
        mRvFavorites.setBackgroundResource(isNightMode()
                ? R.color.colorDarkGray
                : R.color.colorWhite);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_favorite);
        mRvFavorites = (RecyclerView) findViewById(R.id.rv_favorite_list);
        mTvTip = (TextView) findViewById(R.id.tv_tip);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setData();//在显示界面的时候都要刷新数据显示
    }

    /**
     * RecyclerView的配置和数据设置
     */
    private void setData() {
        FavoriteNewsDB favDB = new FavoriteNewsDB();
        List<String> favorites = favDB.listAll();
        if (favorites != null) {
            if (favorites.size() != 0) {
                mRvFavorites.setVisibility(VISIBLE);
                mTvTip.setVisibility(GONE);

                if (mAdapter == null) {
                    mAdapter = new IntentStoryAdapter(this, favorites);
                    mRvFavorites.setLayoutManager(new LinearLayoutManager(this));
                    mRvFavorites.setAdapter(mAdapter);
                } else {
                    mAdapter.setDataAndNotify(favorites);
                }
            } else {
                mRvFavorites.setVisibility(GONE);
                mTvTip.setVisibility(VISIBLE);
            }
        }
    }
}
