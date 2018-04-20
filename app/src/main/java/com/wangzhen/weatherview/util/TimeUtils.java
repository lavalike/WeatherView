package com.wangzhen.weatherview.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by wangzhen on 2018/4/18.
 */
public class TimeUtils {

    /**
     * 格式化日期
     *
     * @param format
     * @param date
     * @return
     */
    public static String formatDate(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 当前日期加1天
     *
     * @param date
     * @return
     */
    public static Date addOneDayForDate(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        return instance.getTime();
    }

    /**
     * 根据日期获得星期几
     *
     * @param date
     * @return
     */
    public static String getWeekForDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}
