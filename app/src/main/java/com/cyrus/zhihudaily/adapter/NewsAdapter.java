package com.cyrus.zhihudaily.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.activity.NewsDetailActivity;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.constants.SharePreferenceConstant;
import com.cyrus.zhihudaily.holder.CardHolder;
import com.cyrus.zhihudaily.holder.HeaderHolder;
import com.cyrus.zhihudaily.models.SimpleStory;
import com.cyrus.zhihudaily.models.LatestNewsData;
import com.cyrus.zhihudaily.models.Story;
import com.cyrus.zhihudaily.models.TopStory;
import com.cyrus.zhihudaily.utils.DateUtils;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
import com.cyrus.zhihudaily.utils.UiUtils;

import java.util.ArrayList;
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
     * 上下文
     */
    private Context mContext;
    /**
     * 新闻结构体
     */
    private LatestNewsData mNewsData;
    /**
     * 头条新闻列表
     */
    private List<TopStory> mTopStories;
    /**
     * 每日新闻列表
     */
    private List<Story> mStories;

    /**
     * 判断当前大图是否轮播
     */
    private boolean mIsRecycle;
    /**
     * 记录当前大图位置
     */
    private int mCurrentTopItem;
    /**
     * 控制自动轮播的任务
     */
    private AutoTask mAutoTask;
    /**
     * 新闻首选项
     */
    private SharedPreferences mNewsSp;
    /*
     * 头条新闻界面的控件
     */
    private ViewPager mHeaderPager;
    private ImageView mIvsGuideSpots[];

    public NewsAdapter(Context context, LatestNewsData newsData) {
        mContext = context;
        mNewsData = newsData;
        mTopStories = mNewsData.getTop_stories();
        mStories = mNewsData.getStories();
        mStories.add(0, null);
        mNewsSp = UiUtils.getContext().getSharedPreferences(SharePreferenceConstant
                .NEWS_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderHolder(View.inflate(parent.getContext(),
                    R.layout.item_top_picture, null));
        } else if (viewType == TYPE_DAILY) {
            return new CardHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily_news, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            mHeaderPager = headerHolder.getViewPager();
            mIvsGuideSpots = headerHolder.getGuideSpots();

            HeaderImageAdapter headerAdapter = new HeaderImageAdapter(mContext, mTopStories);
            mHeaderPager.setAdapter(headerAdapter);
            mHeaderPager.addOnPageChangeListener(this);
            mHeaderPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                        mAutoTask.stop();
                    } else {
                        mAutoTask.start();
                    }
                    return false;
                }
            });
            mHeaderPager.setCurrentItem(mCurrentTopItem);

            //执行头条轮播
            mAutoTask = new AutoTask();
            mAutoTask.start();
        } else if (holder instanceof CardHolder) {
            CardHolder cardHolder = (CardHolder) holder;
            CardView cvItem = cardHolder.getCardView();
            TextView tvTime = cardHolder.getTvTime();
            ImageView ivTitle = cardHolder.getIvTitle();
            final TextView tvTitle = cardHolder.getTvTitle();

            final Story story = mStories.get(position - 1);
            /*
             * 当前位置是否显示日期；
             * 对象为null代表该位置的控件仅仅是用于显示时间轴而不显示卡片
             */
            if (story != null) {
                tvTime.setVisibility(GONE);
                cvItem.setVisibility(VISIBLE);
                //设置卡片标题
                tvTitle.setText(story.getTitle());
                tvTitle.setTextColor(mNewsSp.getBoolean(story.getId(), false)
                        ? Color.GRAY : Color.BLACK);//已点击过则显示灰色，未点击显示黑色
                //设置卡片图片
                LoadImageUtils.loadImage(story.getImages().get(0), ivTitle);
                //设置卡片点击效果和响应事件
                cvItem.setBackgroundResource(R.drawable.btn_bg);
                cvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvTitle.setTextColor(Color.GRAY);
                        mNewsSp.edit().putBoolean(story.getId(), true).apply();//记录已点击

                        SimpleStory simpleStory = new SimpleStory();

                        simpleStory.setId(story.getId());
                        simpleStory.setTitle(story.getTitle());
                        ArrayList<String> images = story.getImages();
                        simpleStory.setImages(images);

                        Intent intent = new Intent(UiUtils.getContext(),
                                NewsDetailActivity.class);
                        intent.putExtra(DataConstant.INTENT_NEWS, simpleStory);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                cvItem.setVisibility(GONE);
                tvTime.setVisibility(VISIBLE);
                if (position == 1) {
                    tvTime.setText(R.string.news_list_today);
                } else {
                    tvTime.setText(DateUtils.convertDate(mStories.get(position).getDate()));
                }
            }
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

    public void addItem(ArrayList<Story> stories) {
        int beforeSize = mStories.size();
        mStories.add(null);
        mStories.addAll(beforeSize + 1, stories);
        notifyItemRangeInserted(beforeSize + 1, stories.size());
    }

    public void setNewsData(LatestNewsData newsData) {
        mNewsData = newsData;
        mTopStories = mNewsData.getTop_stories();
        mStories = mNewsData.getStories();
        mStories.add(0, null);
    }

    /**
     * 用于自动轮播头条图片
     */
    private class AutoTask implements Runnable {
        @Override
        public void run() {
            if (mIsRecycle) {
                UiUtils.cancel(this);//先取消循环的执行
                mCurrentTopItem = mHeaderPager.getCurrentItem();
                mCurrentTopItem = ++mCurrentTopItem % 5;
                mHeaderPager.setCurrentItem(mCurrentTopItem);
                UiUtils.postDelayed(this, 4000);//设置完位置再重新执行循环
            }
        }

        void start() {
            if (!mIsRecycle) {
                UiUtils.cancel(this);
                mIsRecycle = true;
                UiUtils.postDelayed(this, 4000);
            }
        }

        void stop() {
            if (mIsRecycle) {
                mIsRecycle = false;
                UiUtils.cancel(this);
            }
        }
    }

}
