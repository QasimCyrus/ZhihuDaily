package com.cyrus.zhihudaily.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.cyrus.zhihudaily.models.NewsData;
import com.cyrus.zhihudaily.models.Story;
import com.cyrus.zhihudaily.utils.DateUtils;
import com.cyrus.zhihudaily.utils.LoadingNewsUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.cyrus.zhihudaily.view.LoadingPage;

import java.util.ArrayList;

/**
 * 新闻列表
 * <p>
 * Created by Cyrus on 2016/10/12.
 */

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static String sCurrentDate;
    private static String sBeforeDate;

    private RecyclerView mRvNews;
    private NewsData mNewsData;
    private FrameLayout mFlContent;
    private SwipeRefreshLayout mSrlLoad;
    private SharedPreferences mSp;
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
        mRvNews = (RecyclerView) view.findViewById(R.id.rv_news_list);
        mLlManager = new LinearLayoutManager(UiUtils.getContext());
        mRvNews.setLayoutManager(mLlManager);
        mNewsAdapter = new NewsAdapter(mNewsData);
        mRvNews.setAdapter(mNewsAdapter);
        mSrlLoad.setOnRefreshListener(this);

        return view;
    }

    /**
     * 加载新闻最新的新闻
     *
     * @return STATE_ERROR--错误；STATE_EMPTY--空白； STATE_SUCCESS--成功
     */
    private LoadingPage.LoadResult loadNews() {
        ArrayList<Story> stories;
        mNewsData = LoadingNewsUtils.load(GlobalConstant.LatestNewsData);
        if (mNewsData != null) {
            stories = mNewsData.getStories();
            sCurrentDate = mNewsData.getDate();
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
        //TODO 下拉刷新
    }
}
