package com.cyrus.zhihudaily.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.adapter.NewsAdapter;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.LatestNewsData;
import com.cyrus.zhihudaily.models.Story;
import com.cyrus.zhihudaily.utils.DateUtils;
import com.cyrus.zhihudaily.utils.LoadingNewsUtils;
import com.cyrus.zhihudaily.utils.NetUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.cyrus.zhihudaily.view.LoadingPage;

import java.util.ArrayList;

/**
 * 最新新闻列表
 * <p>
 * Created by Cyrus on 2016/10/12.
 */

public class HomeNewsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 记录当前列表已经加载了哪一天的数据
     */
    private static String sBeforeDate;
    /**
     * 当前新闻数据的结构体
     */
    private LatestNewsData mNewsData;
    /**
     * 用于判断当前上拉时是否在加载数据
     */
    private boolean mIsLoading;

    private FrameLayout mFlContent;
    private SwipeRefreshLayout mSrlLoad;
    private NewsAdapter mNewsAdapter;
    private LinearLayoutManager mLlManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mFlContent = (FrameLayout) view.findViewById(R.id.fl_content);
        LoadingPage loadingPage = new LoadingPage(UiUtils.getContext()) {
            @Override
            public View createSuccessView() {
                return successView();
            }

            @Override
            public LoadResult load() {
                return loadNews();
            }
        };

        loadingPage.show();
        mFlContent.addView(loadingPage);

        return view;
    }

    private View successView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.page_success, null);

        mSrlLoad = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        RecyclerView rvNews = (RecyclerView) view.findViewById(R.id.rv_news_list);
        mLlManager = new LinearLayoutManager(UiUtils.getContext());
        rvNews.setLayoutManager(mLlManager);
        mNewsAdapter = new NewsAdapter(getContext(), mNewsData);
        rvNews.setAdapter(mNewsAdapter);
        mSrlLoad.setOnRefreshListener(this);
        rvNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //上拉加载，无网络则加载缓存，无缓存则提示
                    int last = mLlManager.findLastVisibleItemPosition();
                    int totalItemCount = mLlManager.getItemCount();

                    if (last + 1 == totalItemCount) {
                        if (!mIsLoading) {//mIsLoading用于判断当前加载状态
                            mSrlLoad.setRefreshing(true);
                            mIsLoading = true;
                            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final LatestNewsData dailyNewsData = LoadingNewsUtils
                                            .loadLatest(GlobalConstant.BEFORE_NEWS_URL + sBeforeDate);
                                    UiUtils.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (dailyNewsData != null) {
                                                mNewsAdapter.addItem(dailyNewsData.getStories());
                                                sBeforeDate = DateUtils.getBeforeDay(sBeforeDate, 1);
                                                mSrlLoad.setRefreshing(false);
                                            } else {
                                                showDisconnectedInfo();
                                            }
                                            mIsLoading = false;
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });

        return view;
    }

    /**
     * 加载新闻并返回结果码
     *
     * @return STATE_ERROR--错误；STATE_EMPTY--空白； STATE_SUCCESS--成功
     */
    private LoadingPage.LoadResult loadNews() {
        ArrayList<Story> stories;
        mNewsData = LoadingNewsUtils.loadLatest(GlobalConstant.LATEST_NEWS_DATA);
        if (mNewsData != null) {
            stories = mNewsData.getStories();
            String sCurrentDate = mNewsData.getDate();
            sBeforeDate = DateUtils.getBeforeDay(sCurrentDate, 1);
        } else {
            return LoadingPage.LoadResult.ERROR;
        }

        if (stories == null) {
            return LoadingPage.LoadResult.EMPTY;
        } else {
            return LoadingPage.LoadResult.SUCCESS;
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新，无网络则提示
        if (NetUtils.isNetConnectedOrConnecting()) {
            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    loadNews();
                    mNewsAdapter.setNewsData(mNewsData);
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNewsAdapter.notifyDataSetChanged();
                            mSrlLoad.setRefreshing(false);
                        }
                    });
                }
            });
        } else {
            showDisconnectedInfo();
        }
    }

    /**
     * 无网络连接的提示
     */
    private void showDisconnectedInfo() {
        Snackbar.make(mFlContent, R.string.info_internet_disconnected,
                Snackbar.LENGTH_SHORT).show();
        mSrlLoad.setRefreshing(false);
    }
}
