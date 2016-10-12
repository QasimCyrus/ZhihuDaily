package com.cyrus.zhihudaily.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyrus.zhihudaily.utils.UiUtils;

/**
 * Created by Cyrus on 2016/10/12.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "news_data.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "NewsTable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_JSON = "json";

    public DataBaseHelper() {
        super(UiUtils.getContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                COLUMN_ID + " integer primary key autoincrement," +
                COLUMN_DATE + " text," +
                COLUMN_JSON + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
