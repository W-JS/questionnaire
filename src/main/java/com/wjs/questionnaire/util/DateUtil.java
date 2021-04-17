package com.wjs.questionnaire.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期操作
 */
public class DateUtil {

    /**
     * 将日期字符串转化为日期
     *
     * @param dateString yyyy-MM-dd HH:mm:ss 格式的日期字符串
     * @return yyyy-MM-dd HH:mm:ss 格式的Date
     */
    public static Date StringToDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将日期转化为日期字符串
     *
     * @param date yyyy-MM-dd HH:mm:ss 格式的Date
     * @return yyyy-MM-dd HH:mm:ss 格式的日期字符串
     */
    public static String DateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
