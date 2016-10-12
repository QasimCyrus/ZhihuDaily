package com.cyrus.zhihudaily.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.utils.UiUtils;

/**
 * 首页加载状况视图，根据加载情况可以返回不同的页面
 * <p>
 * Created by weics on 2016/4/26.
 */
public abstract class LoadingPage extends FrameLayout {

    /**
     * 未知状态
     */
    public static final int STATE_UNKNOWN = 0;
    /**
     * 加载状态
     */
    public static final int STATE_LOADING = 1;
    /**
     * 错误状态
     */
    public static final int STATE_ERROR = 2;
    /**
     * 无内容状态
     */
    public static final int STATE_EMPTY = 3;
    /**
     * 加载成功状态
     */
    public static final int STATE_SUCCESS = 4;
    /**
     * 初始化状态为未知状态
     */
    private int mState = STATE_UNKNOWN;

    private View mLoadingView;//加载中的界面
    private View mErrorView;//错误界面
    private View mEmptyView;//空界面
    private View mSuccessView;//加载成功的界面

    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        mLoadingView = createLoadingView();
        if (mLoadingView != null) {
            this.addView(mLoadingView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        mErrorView = createErrorView();
        if (mErrorView != null) {
            this.addView(mErrorView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        mEmptyView = createEmptyView();
        if (mEmptyView != null) {
            this.addView(mEmptyView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        showPage();
    }

    public void showPage() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mState == STATE_UNKNOWN
                    || mState == STATE_LOADING
                    ? View.VISIBLE
                    : View.INVISIBLE);
        }

        if (mErrorView != null) {
            mErrorView.setVisibility(mState == STATE_ERROR
                    ? View.VISIBLE
                    : View.INVISIBLE);
        }

        if (mEmptyView != null) {
            mEmptyView.setVisibility(mState == STATE_EMPTY
                    ? View.VISIBLE
                    : View.INVISIBLE);
        }

        if (mState == STATE_SUCCESS) {
            if (mSuccessView == null) {
                mSuccessView = createSuccessView();
                this.addView(mSuccessView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            mSuccessView.setVisibility(View.VISIBLE);
        } else {
            if (mSuccessView != null) {
                mSuccessView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void show() {
        if (mState == STATE_ERROR || mState == STATE_EMPTY) {
            mState = STATE_LOADING;
        }
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                final LoadResult result = load();
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            mState = result.getValue();
                            showPage();
                        }
                    }
                });
            }
        });

        showPage();
    }


    public View createEmptyView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.page_empty, null);
        view.findViewById(R.id.page_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        return view;
    }


    public View createLoadingView() {
        return View.inflate(UiUtils.getContext(), R.layout.page_loading, null);
    }

    public View createErrorView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.page_error, null);
        view.findViewById(R.id.page_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        return view;
    }


    public abstract View createSuccessView();

    public abstract LoadResult load();

    public enum LoadResult {
        ERROR(STATE_ERROR), EMPTY(STATE_EMPTY), SUCCESS(STATE_SUCCESS);
        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
