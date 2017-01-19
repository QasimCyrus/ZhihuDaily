package com.cyrus.zhihudaily.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyrus.zhihudaily.App;

/**
 * 数据库初始化
 * <p>
 * Created by Cyrus on 2016/10/12.
 */

class DataBaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "news_data.db";
    /**
     * 当前数据库版本
     */
    private static final int VERSION = 4;
    /**
     * 创建新闻条目的数据库
     */
    private static final String CREATE_NEWS_TABLE =
            "create table " + NewsTable.NEWS_TABLE_NAME + "(" +
                    NewsTable.COLUMN_ID + " integer primary key autoincrement," +
                    NewsTable.COLUMN_DATE + " text," +
                    NewsTable.COLUMN_JSON + " text)";
    /**
     * 创建收藏新闻的数据库
     */
    private static final String CREATE_FAVORITE_TABLE =
            "create table " + FavoriteTable.FAVORITE_TABLE_NAME + "(" +
                    FavoriteTable.COLUMN_ID + " integer primary key autoincrement," +
                    FavoriteTable.COLUMN_NEWS_ID + " text," +
                    FavoriteTable.COLUMN_JSON + " text)";
    /**
     * 创建详细新闻的数据库
     */
    private static final String CREATE_DETAIL_TABLE =
            "create table " + DetailTable.DETAIL_TABLE_NAME + "(" +
                    DetailTable.COLUMN_ID + " integer primary key autoincrement," +
                    DetailTable.COLUMN_NEWS_ID + " text," +
                    DetailTable.COLUMN_JSON + " text)";
    /**
     * 创建主题新闻的数据库
     */
    private static final String CREATE_THEME_TABLE =
            "create table " + ThemeNewsTable.THEME_TABLE_NAME + "(" +
                    ThemeNewsTable.COLUMN_ID + " integer primary key autoincrement," +
                    ThemeNewsTable.COLUMN_THEME_ID + " text," +
                    ThemeNewsTable.COLUMN_JSON + " text)";

    DataBaseHelper() {
        super(App.getAppComponent().getContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_DETAIL_TABLE);
        db.execSQL(CREATE_THEME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2://添加收藏条目的数据库
                db.execSQL(CREATE_FAVORITE_TABLE);
                break;
            case 3://添加新闻详情页文字缓存的数据库
                db.execSQL(CREATE_DETAIL_TABLE);
                break;
            case 4://添加主题新闻缓存的数据库
                db.execSQL(CREATE_THEME_TABLE);
                break;
        }
    }

    class NewsTable {
        static final String NEWS_TABLE_NAME = "NewsTable";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_JSON = "json";
    }

    class FavoriteTable {
        static final String FAVORITE_TABLE_NAME = "FavoriteTable";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_NEWS_ID = "id";
        static final String COLUMN_JSON = "json";
    }

    class DetailTable {
        static final String DETAIL_TABLE_NAME = "DetailTable";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_NEWS_ID = "id";
        static final String COLUMN_JSON = "json";
    }

    class ThemeNewsTable {
        static final String THEME_TABLE_NAME = "ThemeTable";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_THEME_ID = "theme";
        static final String COLUMN_JSON = "json";
    }

}
