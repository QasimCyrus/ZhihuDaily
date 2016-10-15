package com.cyrus.zhihudaily.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyrus.zhihudaily.utils.UiUtils;

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
    private static final int VERSION = 2;
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

    DataBaseHelper() {
        super(UiUtils.getContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 2://添加收藏条目的数据库
                db.execSQL(CREATE_FAVORITE_TABLE);
                break;
        }
    }

    private class NewsTable {
        private static final String NEWS_TABLE_NAME = "NewsTable";
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_DATE = "date";
        private static final String COLUMN_JSON = "json";
    }

    private class FavoriteTable {
        private static final String FAVORITE_TABLE_NAME = "FavoriteTable";
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_NEWS_ID = "id";
        private static final String COLUMN_JSON = "json";
    }
}
