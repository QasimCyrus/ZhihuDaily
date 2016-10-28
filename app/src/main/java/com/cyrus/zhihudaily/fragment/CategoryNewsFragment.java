package com.cyrus.zhihudaily.fragment;


import android.os.Bundle;
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
import com.cyrus.zhihudaily.adapter.IntentStoryAdapter;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.CategoryNewsData;
import com.cyrus.zhihudaily.models.IntentStory;
import com.cyrus.zhihudaily.models.Story;
import com.cyrus.zhihudaily.utils.LoadingNewsUtils;
import com.cyrus.zhihudaily.utils.NetUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.cyrus.zhihudaily.view.LoadingPage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类新闻列表
 * <p>
 * Created by Cyrus on 2016/10/22.
 */
public class CategoryNewsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSrlLoad;
    private IntentStoryAdapter mAdapter;

    private List<String> mIntentNewsList;
    private FrameLayout mFlContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_news, container, false);

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
        mSrlLoad.setOnRefreshListener(this);

        RecyclerView rvNews = (RecyclerView) view.findViewById(R.id.rv_news_list);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new IntentStoryAdapter(getContext(), mIntentNewsList);
        rvNews.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onRefresh() {
        if (NetUtils.isNetConnectedOrConnecting()) {
            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    loadNews();
                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataChanged(mIntentNewsList);
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
     * 加载新闻并返回结果码
     *
     * @return STATE_ERROR--错误；STATE_EMPTY--空白； STATE_SUCCESS--成功
     */
    private LoadingPage.LoadResult loadNews() {
        List<Story> stories;
        Bundle bundle = getArguments();
        CategoryNewsData newsData;
        if (bundle != null) {
            String id = getArguments().getString(DataConstant.BUNDLE_THEME_ID);
            newsData = LoadingNewsUtils.loadCategory(GlobalConstant.THEME_NEWS_URL + id);
        } else {
            newsData = new CategoryNewsData();
        }

        if (newsData != null) {
            stories = newsData.getStories();
        } else {
            return LoadingPage.LoadResult.ERROR;
        }

        if (stories == null) {
            return LoadingPage.LoadResult.EMPTY;
        } else {
            Gson gson = new Gson();
            mIntentNewsList = new ArrayList<>();

            for (Story story : stories) {
                IntentStory intentStory = new IntentStory();
                intentStory.setId(story.getId());
                intentStory.setTitle(story.getTitle());
                intentStory.setImages(story.getImages());
                mIntentNewsList.add(gson.toJson(story));
            }

            return LoadingPage.LoadResult.SUCCESS;
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

    public void updateTheme() {
        if (mAdapter != null) {
            mAdapter.updateTheme();
        }
    }

}
