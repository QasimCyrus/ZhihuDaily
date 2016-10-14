package com.cyrus.zhihudaily.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.R;

/**
 * 显示新闻卡片的holder
 * <p>
 * Created by Cyrus on 2016/10/14.
 */

public class CardHolder extends RecyclerView.ViewHolder {

    private CardView mCardView;
    private TextView mTvTime;
    private TextView mTvTitle;
    private ImageView mIvTitle;

    public TextView getTvTime() {
        return mTvTime;
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

    public CardHolder(View itemView) {
        super(itemView);

        mCardView = (CardView) itemView.findViewById(R.id.cv_news_item);

        mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mIvTitle = (ImageView) itemView.findViewById(R.id.iv_title);
    }


}
