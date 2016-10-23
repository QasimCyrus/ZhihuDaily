package com.cyrus.zhihudaily.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏新闻的数据库
 * <p>
 * Created by Cyrus on 2016/10/15.
 */

public class FavoriteNewsDB {

    private static final String TABLE_NAME = DataBaseHelper.FavoriteTable.FAVORITE_TABLE_NAME;
    private static final String COLUMN_NEWS_ID = DataBaseHelper.FavoriteTable.COLUMN_NEWS_ID;
    private static final String COLUMN_JSON = DataBaseHelper.FavoriteTable.COLUMN_JSON;

    private DataBaseHelper mHelper;

    public FavoriteNewsDB() {
        mHelper = new DataBaseHelper();
    }

    /**
     * 查找数据库中有无对应ID的数据
     *
     * @param id 要查找的id
     * @return 有查找id的对应数据则返回json数据，没有则返回null
     */
    public boolean find(String id) {
        String result = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_JSON},
                COLUMN_NEWS_ID + "=?", new String[]{id}, null, null, null);

        if (cursor == null) {
            db.close();

            return false;
        } else {
            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }

            cursor.close();
            db.close();

            return result != null;
        }
    }

    /**
     * 获得所有收藏条目的列表
     *
     * @return 返回收藏列表，没有条目则返回null
     */
    public List<String> listAll() {
        List<String> result = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_JSON},
                null, null, null, null, null);

        if (cursor == null) {
            db.close();

            return null;
        } else {
            while (cursor.moveToNext()) {
                String json = cursor.getString(0);
                result.add(json);
            }

            cursor.close();
            db.close();

            return result;
        }
    }

    /**
     * 插入数据
     *
     * @param id   要插入的数据id
     * @param json 要插入的json数据
     */
    public void insert(String id, String json) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NEWS_ID, id);
        contentValues.put(COLUMN_JSON, json);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    /**
     * 删除对应id的数据
     *
     * @param id 要删除数据的id
     */
    public void delete(String id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(TABLE_NAME, COLUMN_NEWS_ID + "=?", new String[]{id});
        db.close();
    }

}
