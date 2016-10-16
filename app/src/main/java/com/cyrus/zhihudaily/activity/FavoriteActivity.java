package com.cyrus.zhihudaily.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.adapter.FavoritesAdapter;
import com.cyrus.zhihudaily.database.FavDB;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        initView();
        initData();
    }

    private void initData() {
        //设置工具栏
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

        //设置列表项，添加到RecyclerView
        FavDB favDB = new FavDB();
        List<String> favorites = favDB.listAll();
        if (favorites != null) {
            FavoritesAdapter adapter = new FavoritesAdapter(this, favorites);
            mRvFavorites.setAdapter(adapter);
        }
        mRvFavorites.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_favorite);
        mRvFavorites = (RecyclerView) findViewById(R.id.rv_favorite_list);
    }
}
