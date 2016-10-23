package com.cyrus.zhihudaily.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 存放新闻详细内容的数据库
 * <p>
 * Created by Cyrus on 2016/10/22.
 */

public class NewsDetailDB {

    private static final String DETAIL_TABLE_NAME = DataBaseHelper.DetailTable.DETAIL_TABLE_NAME;
    private static final String COLUMN_NEWS_ID = DataBaseHelper.DetailTable.COLUMN_NEWS_ID;
    private static final String COLUMN_JSON = DataBaseHelper.DetailTable.COLUMN_JSON;

    private DataBaseHelper mHelper;

    public NewsDetailDB() {
        mHelper = new DataBaseHelper();
    }

    /**
     * 查询对应id的新闻是否有缓存的html
     *
     * @param id 要查询的新闻id
     * @return 有缓存则返回String格式的html，没有则返回null
     */
    public String find(String id) {
        String result = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DETAIL_TABLE_NAME, new String[]{COLUMN_JSON},
                COLUMN_NEWS_ID + "=?", new String[]{id}, null, null, null);

        if (cursor == null) {
            db.close();

            return null;
        } else {
            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }

            cursor.close();
            db.close();

            return result;
        }
    }

    /**
     * 插入一条新闻的详细内容
     *
     * @param id 要插入数据的新闻id
     * @param html 新闻详细内容
     */
    public void insert(String id, String html) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NEWS_ID, id);
        contentValues.put(COLUMN_JSON, html);
        db.insert(DETAIL_TABLE_NAME, null, contentValues);
        db.close();
    }

    /**
     * 更新一条新闻的详细内容
     *
     * @param id 要更新数据的新闻id
     * @param html 新闻详细内容
     */
    public void update(String id, String html) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_JSON, html);
        db.update(DETAIL_TABLE_NAME, contentValues, COLUMN_NEWS_ID + "=?", new String[]{id});
        db.close();
    }

    /**
     * 删除数据库中一条新闻的详细内容
     *
     * @param id 要删除数据的新闻id
     */
    public void delete(String id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(DETAIL_TABLE_NAME, COLUMN_NEWS_ID + "=?", new String[]{id});
        db.close();
    }

    /**
     * 删除数据库中所有数据
     */
    public void deleteAll() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(DETAIL_TABLE_NAME, null, null);
        db.close();
    }
}
