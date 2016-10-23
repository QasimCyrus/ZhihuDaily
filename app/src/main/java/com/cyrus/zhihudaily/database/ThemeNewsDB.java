package com.cyrus.zhihudaily.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Cyrus on 2016/10/23.
 */

public class ThemeNewsDB {

    private static final String THEME_TABLE_NAME = DataBaseHelper.ThemeNewsTable.THEME_TABLE_NAME;
    private static final String COLUMN_THEME_ID = DataBaseHelper.ThemeNewsTable.COLUMN_THEME_ID;
    private static final String COLUMN_JSON = DataBaseHelper.ThemeNewsTable.COLUMN_JSON;

    private DataBaseHelper mHelper;

    public ThemeNewsDB() {
        mHelper = new DataBaseHelper();
    }

    /**
     * 查找数据库中某个主题的json数据
     *
     * @param id 要查找的主题id
     * @return 该主题的json字符串
     */
    public String find(String id) {
        String result = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(THEME_TABLE_NAME, new String[]{COLUMN_JSON},
                COLUMN_THEME_ID + "=?", new String[]{id}, null, null, null);

        if (cursor == null) {
            return null;
        }
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return result;
    }

    /**
     * 插入某个主题的数据
     *
     * @param id 要插入数据的主题ID
     * @param json 该主题的json数据
     */
    public void insert(String id, String json) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_THEME_ID, id);
        contentValues.put(COLUMN_JSON, json);
        db.insert(THEME_TABLE_NAME, null, contentValues);
        db.close();
    }

    /**
     * 更新某个主题的数据
     *
     * @param id 要更新数据的主题ID
     * @param json 更新后的json数据
     */
    public void update(String id, String json) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_JSON, json);
        db.update(THEME_TABLE_NAME, contentValues, COLUMN_THEME_ID + "=?", new String[]{id});
        db.close();
    }

    /**
     * 清空数据库
     */
    public void deleteAll() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(THEME_TABLE_NAME, null, null);
        db.close();
    }

    /**
     * 是否存在某个主题ID的新闻
     *
     * @param id 主题id
     * @return 存在该主题id的内容则返回true，否则返回false
     */
    public boolean isThemeNewsExist(String id) {
        String themeId = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(THEME_TABLE_NAME, new String[]{COLUMN_THEME_ID},
                COLUMN_THEME_ID + "=?", new String[]{id}, null, null, null);

        if (cursor == null) {
            return false;
        }
        if (cursor.moveToNext()) {
            themeId = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return themeId != null;
    }

}
