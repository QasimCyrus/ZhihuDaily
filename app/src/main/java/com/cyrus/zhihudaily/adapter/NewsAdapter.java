package com.cyrus.zhihudaily.adapter;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.holder.CardHolder;
import com.cyrus.zhihudaily.holder.HeaderHolder;
import com.cyrus.zhihudaily.models.NewsData;
import com.cyrus.zhihudaily.models.Story;
import com.cyrus.zhihudaily.models.TopStory;
import com.cyrus.zhihudaily.utils.DateUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 新闻列表适配器
 * <p>
 * Created by Cyrus on 2016/10/12.
 */

public class NewsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ViewPager.OnPageChangeListener {

    /**
     * 头条新闻的标志
     */
    private static final int TYPE_HEADER = 0;
    /**
     * 每日新闻的标志
     */
    private static final int TYPE_DAILY = 1;

    /**
     * 头条新闻列表
     */
    private List<TopStory> mTopStories;
    /**
     * 每日新闻列表
     */
    private List<Story> mStories;

    /*
     * 头条新闻界面的控件
     */
    private ViewPager mHeaderPager;
    private ImageView mIvsGuideSpots[];

    /*
     * 每日新闻界面的控件
     */
    private TextView mTvTime;
    private ImageView mIvTitle;
    private TextView mTvTitle;

    private String mLastDate;

    public NewsAdapter(NewsData newsData) {
        mTopStories = newsData.getTop_stories();
        mStories = newsData.getStories();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(View.inflate(parent.getContext(),
                    R.layout.item_top_picture, null));
        } else if (viewType == TYPE_DAILY) {
            return new CardHolder(View.inflate(parent.getContext(),
                    R.layout.item_news, null));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            mHeaderPager = headerHolder.getViewPager();
            mIvsGuideSpots = headerHolder.getGuideSpots();

            HeaderImageAdapter headerAdapter = new HeaderImageAdapter(mTopStories);
            mHeaderPager.setAdapter(headerAdapter);
            mHeaderPager.addOnPageChangeListener(this);

            //执行头条轮播
            AutoTask autoTask = new AutoTask();
            UiUtils.postDelayed(autoTask, 4000);
        } else if (holder instanceof CardHolder) {
            CardHolder cardHolder = (CardHolder) holder;
            mTvTime = cardHolder.getTvTime();
            mIvTitle = cardHolder.getIvTitle();
            mTvTitle = cardHolder.getTvTitle();

            Story story = mStories.get(position - 1);
            if (position == 1) {//position为0是头条，从position为1开始是每日新闻
                mTvTime.setVisibility(VISIBLE);
                mTvTime.setText(R.string.news_list_today);
                mLastDate = story.getDate();
            } else {
                if (mLastDate.equals(story.getDate())) {
                    mTvTime.setVisibility(GONE);//跟参数mLastDate同个时间的新闻，不需要再显示日期
                } else {
                    // 遇到第一个与mLastDate时间不同的item，则设置其时间可见，
                    // 并把mLastDate设置成当前时间
                    mTvTime.setVisibility(VISIBLE);
                    mTvTime.setText(DateUtils.convertDate(story.getDate()));
                    mLastDate = story.getDate();
                }
            }
            mTvTitle.setText(story.getTitle());
            Picasso.with(UiUtils.getContext()).load(story.getImages().get(0)).into(mIvTitle);
        }
    }

    @Override
    public int getItemCount() {
        return mStories.size() + 1;//+1是top_story的数量
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_DAILY;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        //根据当前头条的不同设置引导点的状态
        for (int i = 0; i < mIvsGuideSpots.length; i++) {
            if (i == position) {
                mIvsGuideSpots[i].setImageResource(R.drawable.guide_point_selected);
            } else {
                mIvsGuideSpots[i].setImageResource(R.drawable.guide_point);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 用于自动轮播头条图片
     */
    private class AutoTask implements Runnable {
        @Override
        public void run() {
            UiUtils.cancel(this);//先取消循环的执行
            int currentItem = mHeaderPager.getCurrentItem();
            currentItem++;
            if (currentItem >= 5) {
                currentItem = 0;
            }
            mHeaderPager.setCurrentItem(currentItem);
            UiUtils.postDelayed(this, 4000);//设置完位置再重新执行循环
        }
    }

}
