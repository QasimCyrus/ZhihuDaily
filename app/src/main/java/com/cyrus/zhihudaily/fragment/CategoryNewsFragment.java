package com.cyrus.zhihudaily.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.adapter.SimpleStoryAdapter;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.CategoryNewsData;
import com.cyrus.zhihudaily.models.SimpleStory;
import com.cyrus.zhihudaily.models.Story;
import com.cyrus.zhihudaily.utils.LoadingNewsUtils;
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
    private SimpleStoryAdapter mAdapter;
    private LoadingPage mLoadingPage;

    private List<String> mSimpleNewsList;
    private String mThemeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_news, container, false);

        FrameLayout flContent = (FrameLayout) view.findViewById(R.id.fl_content);
        mLoadingPage = new LoadingPage(UiUtils.getContext()) {
            @Override
            public View createSuccessView() {
                return successView();
            }

            @Override
            public LoadResult load() {
                return loadNews();
            }
        };

        mLoadingPage.show();
        flContent.addView(mLoadingPage);

        return view;
    }

    private View successView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.page_success, null);
        mSrlLoad = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        RecyclerView rvNews = (RecyclerView) view.findViewById(R.id.rv_news_list);

        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SimpleStoryAdapter(getContext(), mSimpleNewsList);
        rvNews.setAdapter(mAdapter);
        mSrlLoad.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onRefresh() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                loadNews();
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataChanged(mSimpleNewsList);
                        mSrlLoad.setRefreshing(false);
                        mLoadingPage.show();
                    }
                });
            }
        });
    }

    /**
     * 加载新闻并返回结果码
     *
     * @return STATE_ERROR--错误；STATE_EMPTY--空白； STATE_SUCCESS--成功
     */
    private LoadingPage.LoadResult loadNews() {
        List<Story> stories;
        CategoryNewsData newsData = LoadingNewsUtils
                .loadCategory(GlobalConstant.THEME_NEWS_URL + mThemeId);

        if (newsData != null) {
            stories = newsData.getStories();
        } else {
            return LoadingPage.LoadResult.ERROR;
        }

        if (stories == null) {
            return LoadingPage.LoadResult.EMPTY;
        } else {
            Gson gson = new Gson();
            mSimpleNewsList = new ArrayList<>();

            for (Story story : stories) {
                SimpleStory simpleStory = new SimpleStory();
                simpleStory.setId(story.getId());
                simpleStory.setTitle(story.getTitle());
                simpleStory.setImages(story.getImages());
                mSimpleNewsList.add(gson.toJson(story));
            }

            return LoadingPage.LoadResult.SUCCESS;
        }
    }

    public void setThemeId(String id) {
        mThemeId = id;
        if (mAdapter != null) {
            mSrlLoad.setRefreshing(true);
            onRefresh();
        }
    }

}
