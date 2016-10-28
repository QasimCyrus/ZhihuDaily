package com.cyrus.zhihudaily.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cyrus.zhihudaily.BaseActivity;
import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.DataConstant;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.database.FavoriteNewsDB;
import com.cyrus.zhihudaily.database.NewsDetailDB;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.IntentStory;
import com.cyrus.zhihudaily.models.NetNewsData;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
import com.cyrus.zhihudaily.utils.NetUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class NewsDetailActivity extends BaseActivity {

    private FavoriteNewsDB mFavDB;
    private NewsDetailDB mDetailDB;

    private Toolbar mToolbar;
    private ImageView mIvCover;
    private WebView mWvContent;
    private ProgressBar mPbLoading;

    private boolean mIsFavorite;
    private String mNewsId;
    private String mDetailJson;

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
            NetNewsData netNewsData = gson.fromJson(result, NetNewsData.class);

            //加载封面
            LoadImageUtils.loadImage(netNewsData.getImage(), mIvCover);

            //设置手机端样式的html内容并加载
            if (netNewsData.getBody() == null) {
                if (NetUtils.isNetConnectedOrConnecting()) {
                    mWvContent.loadUrl(netNewsData.getShareUrl());
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
                String html = "<html><head>" + css + "</head></body>" +
                        netNewsData.getBody() + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                mWvContent.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
            }

            mWvContent.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //设置为true，链接会在WebView中打开，设为false会在第三方浏览器打开
                    return true;
                }
            });
            mWvContent.setWebChromeClient(new WebChromeClient() {
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
            });
        }
    }

    private void initData() {
        //当前新闻是否已收藏以及是否存有html内容
        mFavDB = new FavoriteNewsDB();
        mDetailDB = new NewsDetailDB();

        final IntentStory intentStory = (IntentStory) getIntent()
                .getSerializableExtra(DataConstant.INTENT_NEWS);
        mNewsId = intentStory.getId();
        mIsFavorite = mFavDB.find(mNewsId);
        mDetailJson = mDetailDB.find(mNewsId);

        //工具栏设置
        mToolbar.setTitle(intentStory.getTitle());//设置标题要放在setSupportActionBar之前
        setSupportActionBar(mToolbar);//一定要放在响应点击事件前面，点击事件的响应才会生效
        mToolbar.setBackgroundColor(UiUtils.setColor(R.color.colorTransparentWhite));
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
                    case R.id.btn_favorite:
                        mIsFavorite = !mIsFavorite;

                        item.setIcon(mIsFavorite
                                ? R.drawable.favorite_pressed
                                : R.drawable.favorite_normal);

                        if (mIsFavorite) {
                            mFavDB.insert(mNewsId, generateJSON(intentStory));
                        } else {
                            mFavDB.delete(mNewsId);
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
     * @param intentStory 从意图中传入的新闻
     * @return 当前意图新闻的json字符串
     */
    private String generateJSON(final IntentStory intentStory) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(intentStory, IntentStory.class);
        return gson.toJson(element);
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("id", intentStory.getId());
//            jsonObject.put("title", intentStory.getTitle());
//            jsonObject.put("images", intentStory.getImages());
//
//            return jsonObject.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings webSettings = mWvContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        mIvCover = (ImageView) findViewById(R.id.iv_cover);
        mWvContent = (WebView) findViewById(R.id.wv_content);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading_detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        MenuItem menuItem = menu.findItem(R.id.btn_favorite);

        if (mIsFavorite) {
            menuItem.setIcon(R.drawable.favorite_pressed);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack()) {
            mWvContent.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
