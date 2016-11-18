package com.cyrus.zhihudaily.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.database.FavoriteNewsDB;
import com.cyrus.zhihudaily.database.NewsDetailDB;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.NewsDetailData;
import com.cyrus.zhihudaily.models.SimpleStory;
import com.cyrus.zhihudaily.utils.ImageUtils;
import com.cyrus.zhihudaily.utils.NetUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class NewsDetailActivity extends BaseActivity {

    /**
     * 收藏新闻列表的数据库
     */
    private FavoriteNewsDB mFavDB;
    /**
     * 存放新闻详细信息的数据库
     */
    private NewsDetailDB mDetailDB;

    /**
     * 判断当前新闻是否已收藏
     */
    private boolean mIsFavorite;
    /**
     * 当前新闻ID
     */
    private String mNewsId;
    /**
     * 当前新闻数据的Json类型
     */
    private String mDetailJson;
    /**
     * 第一次加载的页面内容
     */
    private String mHtml;
    /**
     * 要保存的图片URL
     */
    private String mImageUrl;
    /**
     * 判断是否按回退键时需要后退
     */
    private boolean mShouldGoBack;

    private Toolbar mToolbar;
    private WebView mWvContent;
    private ProgressBar mPbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initView();
        initData();
        initWebView();
        loadNews();
    }

    private void loadNews() {
        if (!NetUtils.isNetConnectedOrConnecting()) {
            if (mDetailJson != null) {
                loadWeb(mDetailJson);
            } else {
                Snackbar.make(mWvContent, R.string.info_internet_disconnected,
                        Snackbar.LENGTH_SHORT).show();
            }
        } else {
            ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    String url = GlobalConstant.DETAIL_URL + mNewsId;
                    final String result = NetUtils.load(url);
                    mDetailDB.insert(mNewsId, result);

                    UiUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadWeb(result);
                        }
                    });
                }
            });
        }

    }

    private void loadWeb(final String result) {
        if (result != null) {
            Gson gson = new Gson();
            NewsDetailData newsDetailData = gson.fromJson(result, NewsDetailData.class);

            //设置手机端样式的html内容并加载
            if (newsDetailData.getBody() == null) {
                if (NetUtils.isNetConnectedOrConnecting()) {
                    mWvContent.loadUrl(newsDetailData.getShareUrl());
                } else {
                    Snackbar.make(mWvContent, R.string.info_internet_disconnected,
                            Snackbar.LENGTH_SHORT).show();
                }
            } else {
                String css;
                if (isNightMode()) {
                    css = "<link rel=\"stylesheet\" " +
                            "href=\"file:///android_asset/style_night.css\" " +
                            "type=\"text/css\">";
                } else {
                    css = "<link rel=\"stylesheet\" " +
                            "href=\"file:///android_asset/style_light.css\" " +
                            "type=\"text/css\">";
                }
                String html = "<html><head>" + css + "</head><body>" +
                        newsDetailData.getBody() + "</body></html>";

                // 不去除headline标题行会导致分类新闻的css文件有问题
                String headline = "<div class=\"headline\">";
                int beginIndex = html.indexOf(headline);
                int endIndex = html.indexOf("</div>", beginIndex);
                headline = html.substring(beginIndex, endIndex);
                mHtml = html.replace(headline, "<div class=\"headline\">" +
                        "<img src=" + newsDetailData.getImage() + "></div>");

                mWvContent.loadDataWithBaseURL("x-data://base", mHtml, "text/html", "UTF-8", null);
            }
        }
    }

    private void initData() {
        //当前新闻是否已收藏以及是否存有html内容
        mFavDB = new FavoriteNewsDB();
        mDetailDB = new NewsDetailDB();

        final SimpleStory simpleStory = (SimpleStory) getIntent()
                .getSerializableExtra(DataConstant.INTENT_NEWS);
        mNewsId = simpleStory.getId();
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                mIsFavorite = mFavDB.find(mNewsId);
                mDetailJson = mDetailDB.find(mNewsId);
            }
        });

        //工具栏设置
        mToolbar.setTitle(simpleStory.getTitle());//设置标题要放在setSupportActionBar之前
        setSupportActionBar(mToolbar);//一定要放在响应点击事件前面，点击事件的响应才会生效
        @ColorInt int color = isNightMode()
                ? UiUtils.getColor(R.color.colorGrayBlack)
                : UiUtils.getColor(R.color.colorPrimary);
        mToolbar.setBackgroundColor(color);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_favorite:
                        mIsFavorite = !mIsFavorite;

                        item.setIcon(mIsFavorite
                                ? R.drawable.favorite_pressed
                                : R.drawable.favorite_normal);

                        if (mIsFavorite) {
                            mFavDB.insert(mNewsId, generateJSON(simpleStory));
                        } else {
                            mFavDB.delete(mNewsId);
                        }

                        return true;
                    case R.id.menu_item_refresh:
                        if ("about:blank".equals(mWvContent.getUrl())) {
                            mWvContent.loadDataWithBaseURL("x-data://base",
                                    mHtml, "text/html", "UTF-8", null);
                        } else {
                            mWvContent.reload();
                        }
                        return true;
                }
                return false;
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 将当前传入的新闻转换成json类型
     *
     * @param simpleStory 从意图中传入的新闻
     * @return 当前意图新闻的json字符串
     */
    private String generateJSON(final SimpleStory simpleStory) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(simpleStory, SimpleStory.class);
        return gson.toJson(element);
    }

    /**
     * 初始化WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWvContent.setWebViewClient(new NewsWebViewClient());
        mWvContent.setWebChromeClient(new NewsWebChromeClient());
        registerForContextMenu(mWvContent);// 注册WebView的上下文菜单

        // 设置WebView参数
        WebSettings webSettings = mWvContent.getSettings();
        webSettings.setJavaScriptEnabled(true); // 开启JavaScript
        webSettings.setDomStorageEnabled(true); // 开启DOM
        webSettings.setDatabaseEnabled(true); // 设置数据库开启
        webSettings.setAppCacheEnabled(true); // 开启缓存机制
        // 在安卓5.0之后开启混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        // 设置缓存, 有WiFi则不使用缓存
        if (NetUtils.isWifi()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        mWvContent = (WebView) findViewById(R.id.wv_content);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_favorite);

        if (mIsFavorite) {
            menuItem.setIcon(R.drawable.favorite_pressed);
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem
                .OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle() == "保存到手机") {
                    if (NetUtils.isNetConnectedOrConnecting()) {
                        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                final boolean isSuccessful = ImageUtils.saveImage(mImageUrl);
                                UiUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(mWvContent, isSuccessful
                                                        ? "保存成功"
                                                        : "保存失败",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else {
                        Snackbar.make(mWvContent, "当前无网络",
                                Snackbar.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        };

        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE
                        || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    mImageUrl = result.getExtra();
                    menu.add(Menu.NONE, v.getId(), Menu.NONE, "保存到手机")
                            .setOnMenuItemClickListener(onMenuItemClickListener);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack() && mShouldGoBack) {
            mWvContent.goBack();
            if (!mWvContent.canGoBack() && mShouldGoBack) {
                mWvContent.loadDataWithBaseURL("x-data://base", mHtml, "text/html", "UTF-8", null);
                mShouldGoBack = false;
            }
        } else {
            super.onBackPressed();
        }
    }

    private class NewsWebViewClient extends WebViewClient {
        // JS注入：夜间模式的代码
        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setBlockNetworkImage(false);// 加载网页图片
            // TODO 优化css
            if (isNightMode()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    view.evaluateJavascript("document.body.style.backgroundColor=\"#444444\";" +
                            "document.body.style.color=\"white\";", null);
                } else {
                    view.loadUrl("javascript:document.body.style.backgroundColor=\"#444444\";" +
                            "document.body.style.color=\"white\";");
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // 阻塞图片加载，网页加载完毕再进行图片加载
            view.getSettings().setBlockNetworkImage(true);

            MenuItem menuItem_favorite = mToolbar.getMenu().getItem(1);
            if ("x-data://base".equals(url)) {
                menuItem_favorite.setVisible(true);
            } else {
                menuItem_favorite.setVisible(false);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mShouldGoBack = true;
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private class NewsWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //进度条加载
            if (newProgress == 100) {
                mPbLoading.setVisibility(View.GONE);
            } else {
                mPbLoading.setVisibility(View.VISIBLE);
                mPbLoading.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            mToolbar.setTitle(title);
        }
    }

}
