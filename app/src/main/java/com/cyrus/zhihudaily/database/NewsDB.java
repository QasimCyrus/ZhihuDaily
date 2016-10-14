package com.cyrus.zhihudaily.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Cyrus on 2016/10/12.
 */

public class NewsDB {

    private static final String TABLE_NAME = "NewsTable";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_JSON = "json";
    private DataBaseHelper mHelper;

    public NewsDB() {
        mHelper = new DataBaseHelper();
    }

    /**
     * 查找数据库中的json数据
     *
     * @param date 要找的数据的日期
     * @return 查找到的json结果，如果找不到则返回null
     */
    public String find(String date) {
        String result = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_JSON},
                COLUMN_DATE + "=?", new String[]{date}, null, null, null);

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
     * 插入数据
     *
     * @param date 要插入的数据的时间
     * @param json 要插入的json数据
     */
    public void insert(String date, String json) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_JSON, json);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    /**
     * 更新数据
     *
     * @param date 要更新的数据的时间
     * @param json 要更新成为的json数据
     */
    public void update(String date, String json) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_JSON, json);
        db.update(TABLE_NAME, contentValues, COLUMN_DATE+"=?", new String[]{date});
        db.close();
    }

    /**
     * 删除数据
     *
     * @param date 要删除数据的日期
     */
    public void delete(String date) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        db.delete(TABLE_NAME, COLUMN_DATE, new String[]{date});
        db.close();
    }

    /**
     * 是否存在某一天的数据
     *
     * @param date 要查询的日期
     * @return true表示存在当天数据，false表示不存在当天数据
     */
    public boolean isTodayDataExist(String date) {
        String result = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_DATE},
                COLUMN_DATE + "=?", new String[]{date}, null, null, null);

        if (cursor == null) {
            return false;
        }
        if (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return result != null;
    }

}
