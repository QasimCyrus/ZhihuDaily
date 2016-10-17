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
import com.cyrus.zhihudaily.adapter.FavoritesAdapter;
import com.cyrus.zhihudaily.database.FavDB;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FavoriteActivity extends BaseActivity {

    private Toolbar mToolbar;
    private RecyclerView mRvFavorites;
    private TextView mTvTip;
    private FavoritesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        initToolbar();
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

        setData();
    }

    /**
     * RecyclerView的配置和数据设置
     */
    private void setData() {
        FavDB favDB = new FavDB();
        List<String> favorites = favDB.listAll();
        if (favorites != null) {
            if (favorites.size() != 0) {
                mRvFavorites.setVisibility(VISIBLE);
                mTvTip.setVisibility(GONE);

                if (mAdapter == null) {
                    mAdapter = new FavoritesAdapter(this, favorites);
                    mRvFavorites.setLayoutManager(new LinearLayoutManager(this));
                    mRvFavorites.setAdapter(mAdapter);
                } else {
                    mAdapter.setData(favorites);
                    mAdapter.notifyDataSetChanged();
                }
            }else {
                mRvFavorites.setVisibility(GONE);
                mTvTip.setVisibility(VISIBLE);
            }
        }
    }
}
