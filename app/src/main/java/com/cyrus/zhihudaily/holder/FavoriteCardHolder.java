package com.cyrus.zhihudaily.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.R;

/**
 * 收藏新闻条目的卡片holder
 * <p>
 * Created by Cyrus on 2016/10/16.
 */

public class FavoriteCardHolder extends RecyclerView.ViewHolder{

    private CardView mCardView;
    private TextView mTvTitle;
    private ImageView mIvTitle;

    public FavoriteCardHolder(View itemView) {
        super(itemView);

        mCardView = (CardView) itemView.findViewById(R.id.cv_news_item);

        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mIvTitle = (ImageView) itemView.findViewById(R.id.iv_title);
    }

    public TextView getTvTitle() {
        return mTvTitle;
    }

    public ImageView getIvTitle() {
        return mIvTitle;
    }

    public CardView getCardView() {
        return mCardView;
    }

}
