package com.cyrus.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.activity.NewsDetailActivity;
import com.cyrus.zhihudaily.constants.IntentConstant;
import com.cyrus.zhihudaily.holder.FavoriteCardHolder;
import com.cyrus.zhihudaily.models.IntentStory;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏新闻条目的适配器
 * <p>
 * Created by Cyrus on 2016/10/16.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mFavorites;
    private List<IntentStory> mStories;

    public FavoritesAdapter(Context context, List<String> favorites) {
        mContext = context;
        mFavorites = favorites;
        parseJson();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteCardHolder(View.inflate(UiUtils.getContext(),
                R.layout.item_news_card, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FavoriteCardHolder favCardHolder = (FavoriteCardHolder) holder;
        final IntentStory intentStory = mStories.get(position);
        favCardHolder.getTvTitle().setText(intentStory.getTitle());
        LoadImageUtils.loadImage(intentStory.getImages().get(0), favCardHolder.getIvTitle());
        favCardHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UiUtils.getContext(), NewsDetailActivity.class);
                intent.putExtra(IntentConstant.INTENT_NEWS, intentStory);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    private void parseJson() {
        Gson gson = new Gson();
        mStories = new ArrayList<>();

        for (String json : mFavorites) {
            IntentStory intentStory = gson.fromJson(json, IntentStory.class);
            mStories.add(intentStory);
        }
    }

}
