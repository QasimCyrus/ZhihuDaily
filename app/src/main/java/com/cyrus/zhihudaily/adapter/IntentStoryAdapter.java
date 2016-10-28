package com.cyrus.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.activity.NewsDetailActivity;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.constants.SharePreferenceConstant;
import com.cyrus.zhihudaily.holder.FavoriteCardHolder;
import com.cyrus.zhihudaily.models.IntentStory;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单新闻条目的适配器
 * <p>
 * Created by Cyrus on 2016/10/16.
 */

public class IntentStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 新闻列表的json
     */
    private List<String> mStoriesString;
    /**
     * 转换为结构体的新闻列表
     */
    private List<IntentStory> mStories;
    /**
     * 新闻首选项
     */
    private SharedPreferences mNewsSp;
    /**
     * 判断当前是夜间模式还是日间模式，true为夜间模式
     */
    private boolean mIsNightMode;

    public IntentStoryAdapter(Context context, List<String> storiesString) {
        mContext = context;
        mStoriesString = storiesString;
        parseJson();
        mNewsSp = UiUtils.getContext().getSharedPreferences(SharePreferenceConstant
                .NEWS_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mIsNightMode = UiUtils.getContext().getSharedPreferences(
                SharePreferenceConstant.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getBoolean(SharePreferenceConstant.IS_NIGHT_MODE, false);
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
        CardView cvItem = favCardHolder.getCardView();
        final TextView tvTitle = favCardHolder.getTvTitle();
        ImageView ivTitle = favCardHolder.getIvTitle();

        //设置卡片标题
        tvTitle.setText(intentStory.getTitle());
        if (mIsNightMode) {
            tvTitle.setTextColor(mNewsSp.getBoolean(intentStory.getId(), false)
                    ? 0xFFBBBBBB
                    : 0xEEDDDDDD);
        } else {
            tvTitle.setTextColor(mNewsSp.getBoolean(intentStory.getId(), false)
                    ? Color.GRAY
                    : Color.BLACK);
        }
        //设置卡片图片
        ArrayList<String> images = intentStory.getImages();
        if (images != null) {
            LoadImageUtils.loadImage(intentStory.getImages().get(0), ivTitle);
        } else {
            ivTitle.setImageResource(R.drawable.ic_empty_page);
        }
        //设置卡片点击效果和相应事件
        cvItem.setBackgroundResource(mIsNightMode
                ? R.drawable.card_bg_night
                : R.drawable.card_bg_light);
        cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setTextColor(mIsNightMode
                        ? 0xFFBBBBBB
                        : Color.GRAY);
                mNewsSp.edit().putBoolean(intentStory.getId(), true).apply();

                Intent intent = new Intent(UiUtils.getContext(), NewsDetailActivity.class);
                intent.putExtra(DataConstant.INTENT_NEWS, intentStory);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStoriesString.size();
    }

    /**
     * 将新闻String类型的json数据列表转换为新闻模型列表
     */
    private void parseJson() {
        Gson gson = new Gson();
        mStories = new ArrayList<>();

        for (String json : mStoriesString) {
            IntentStory intentStory = gson.fromJson(json, IntentStory.class);
            mStories.add(intentStory);
        }
    }

    /**
     * 设置数据更新，该方法用于新闻收藏的界面更新
     *
     * @param newStoriesString 更新后的数据
     */
    public void setDataAndNotify(List<String> newStoriesString) {
        int position;//数据更新的位置
        int newSize = newStoriesString.size();

        for (int i = 0; i < newSize; i++) {
            if (!mStoriesString.get(i).equals(newStoriesString.get(i))) {//找到数据更新的位置
                position = i;
                if (mStoriesString.size() == newSize) {//如果用户是取消了收藏又重新收藏
                    mStoriesString = newStoriesString;
                    notifyItemMoved(position, newSize - 1);
                } else {//用户取消了收藏
                    mStoriesString = newStoriesString;
                    notifyItemRemoved(position);
                }
                return;
            }
        }

        //如果删除的位置是最后一个数据
        position = newSize;
        mStoriesString = newStoriesString;
        notifyItemRemoved(position);
    }

    /**
     * 设置数据，并更新列表视图
     *
     * @param newStoriesString 更新后的数据
     */
    public void notifyDataChanged(List<String> newStoriesString) {
        mStoriesString = newStoriesString;
        parseJson();//关键语句，否则更新视图会错位
        notifyDataSetChanged();
    }

    public void updateTheme() {
        mIsNightMode = ((BaseActivity) mContext).isNightMode();
        notifyDataSetChanged();
    }
}
