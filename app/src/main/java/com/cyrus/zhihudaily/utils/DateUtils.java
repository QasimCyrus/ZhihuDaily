package com.cyrus.zhihudaily.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类
 * <p>
 * Created by Cyrus on 2016/5/1.
 */
public class DateUtils {

    /**
     * 默认日期格式
     */
    private static final String FORMAT = "yyyyMMdd";

    /**
     * 获得当前时间
     *
     * @param strFormat 日期格式
     * @return 按格式输出的日期
     */
    public static String getUserDate(String strFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(strFormat, Locale.US);
        return formatter.format(currentTime);
    }

    /**
     * 得到给定日期的前N天
     *
     * @param nowDate 当前日期
     * @param before 日期差
     * @return 当前日期的前N天
     */
    public static String getBeforeDay(String nowDate, int before) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT, Locale.US);
        Date date = strToDate(nowDate);
        long myTime = (date.getTime() / 1000) - before * 24 * 60 * 60;
        date.setTime(myTime * 1000);
        return sdf.format(date);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate 要转换的String类型日期
     * @return 转换为yyyy-MM-dd格式的时间
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT, Locale.US);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(strDate, pos);
    }

    public static String convertDate(String date) {
        String result = date.substring(0, 3);
        result += "年";
        result += date.substring(4, 6);
        result += "月 ";
        result += date.substring(6, 8);
        result += "日 ";
        result += dayForWeek(date);
        return result;
    }

    private static String dayForWeek(String pTime) {
        String str = "";
        SimpleDateFormat format = new SimpleDateFormat(FORMAT, Locale.US);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
            int dayForWeek;
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            switch (dayForWeek) {
                case 1:
                    str = "星期一";
                    break;
                case 2:
                    str = "星期二";
                    break;
                case 3:
                    str = "星期三";
                    break;
                case 4:
                    str = "星期四";
                    break;
                case 5:
                    str = "星期五";
                    break;
                case 6:
                    str = "星期六";
                    break;
                case 7:
                    str = "星期日";
                    break;
            }
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

}
