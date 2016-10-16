package com.cyrus.zhihudaily.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cyrus.zhihudaily.R;
import com.cyrus.zhihudaily.constants.GlobalConstant;
import com.cyrus.zhihudaily.constants.IntentConstant;
import com.cyrus.zhihudaily.database.FavDB;
import com.cyrus.zhihudaily.manager.ThreadManager;
import com.cyrus.zhihudaily.models.IntentStory;
import com.cyrus.zhihudaily.models.NetNewsData;
import com.cyrus.zhihudaily.utils.LoadImageUtils;
import com.cyrus.zhihudaily.utils.NetUtils;
import com.cyrus.zhihudaily.utils.UiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MenuItem mMenuItem;
    private ImageView mIvCover;
    private FavDB mFavDB;
    private WebView mWvContent;
    private boolean mIsFavorite;
    private String mNewsId;
    private String mHtml;

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
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                String url = GlobalConstant.DETAIL_URL + mNewsId;
                final String result = NetUtils.load(url);
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadWeb(result);
                    }
                });
            }
        });
    }

    private void loadWeb(String result) {
        if (result != null) {
            Gson gson = new Gson();
            final NetNewsData netNewsData = gson.fromJson(result, NetNewsData.class);

            //加载封面
            LoadImageUtils.loadImage(netNewsData.getImage(), mIvCover);

            //设置手机端样式的html内容并加载
            String css = "<link rel=\"stylesheet\" " +
                    "href=\"file:///android_asset/style_light.css\" type=\"text/css\">";
            mHtml = "<html><head>" + css + "</head></body>" +
                    netNewsData.getBody() + "</body></html>";
            mHtml = mHtml.replace("<div class=\"img-place-holder\">", "");
            mWvContent.loadDataWithBaseURL("x-data://base", mHtml, "text/html", "UTF-8", null);

            mWvContent.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    //设置为true，链接会在WebView中打开，设为false会在第三方浏览器打开
                    return true;
                }
            });
        }
    }

    private void initData() {
        //当前新闻是否已收藏
        mFavDB = new FavDB();
        final IntentStory intentStory = (IntentStory) getIntent()
                .getSerializableExtra(IntentConstant.INTENT_NEWS);
        mNewsId = intentStory.getId();
        mIsFavorite = mFavDB.find(mNewsId);

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
//            Log.d("hello", jsonObject.toString());
//            return jsonObject.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        mMenuItem = menu.findItem(R.id.btn_favorite);

        if (mIsFavorite) {
            mMenuItem.setIcon(R.drawable.favorite_pressed);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWvContent.canGoBack()) {
            mWvContent.goBack();
        } else {
            finish();
        }
    }

}
