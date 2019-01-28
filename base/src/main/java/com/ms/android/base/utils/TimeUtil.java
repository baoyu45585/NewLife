package com.ms.android.base.utils;/**
 * Created by del on 17/4/12.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * created by lbw at 17/4/12
 */
public class TimeUtil {
    public static final String YY_MM_DD = "yyyy.MM.dd";
    public static final String YY_MM_DD_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String YY_MM_DD_H_M = "yyyy.MM.dd HH:mm";
    public static String getTime(long sum) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy.MM.dd HH:mm");
        Date curDate = new Date(sum);// 获取当前时间
        String dtime = formatter.format(curDate);
        return dtime;
    }

    // 获取当前 月 日
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy.MM.dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dtime = formatter.format(curDate);
        return dtime;
    }

    // 获取当前机器时间
    public static String getLocationTime(String str) {
            SimpleDateFormat formatter = new SimpleDateFormat(
                str);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dtime = formatter.format(curDate);
        return dtime;
    }

    // Long（毫秒） 转成 String
    public static String getTimeLongOfString(String str,Long aLong) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                str);
        Date curDate = new Date(aLong);
        String dtime = formatter.format(curDate);
        return dtime;
    }


    //获取当天晚上12点
    public static long getNow24() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dtime = formatter.format(curDate);
        dtime = dtime + " 24:00";
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = formatter2.parse(dtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date.getTime();
    }
    //获取当天某个时间
    public static long getTimeLong(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dtime = formatter.format(curDate);
        dtime = dtime +" "+ time;
        if (dtime.contains("：")){
            dtime= dtime.replace("：",":");
        }
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = formatter2.parse(dtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getHMSString() {
        Date date=new Date();
       return dataOfString(date,"HH:mm");
    }

    //获取当天某个时间
    public static String getTimeString(String time) {
        if (time == null || "".equals(time)){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dtime = formatter.format(curDate);
        dtime = (dtime + time +"00").replaceAll(":","");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = formatter2.parse(dtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter2.format(date);
    }





    //String 转 long
    public static long getStringToLong(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }




    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 根据日期获得星期
     *
     * @param
     * @return
     */
    public static String getWeekOfDate() {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 根据string日期获取date
     */
    public static Date StringOfDate(String time,String simpleDateFormat)
    {
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 根据date日期获取string
     */
    public static String dataOfString(Date date,String simpleDateFormat)
    {
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
        String dtime = format.format(date);
        return dtime;
    }

    /**
     * MM月dd日  星期m
     */
    public static String getWeatherTime(String time)
    {
        Date date = StringOfDate(time,"yyyy-MM-dd");

        return dataOfString(date,"MM月dd日")+"        "+getWeekOfDate(date);
    }

    /**
     * yyyy/MM/dd  星期m
     */
    public static String getSplashTime() {
        Date date = new Date();
        return dataOfString(date,"yyyy-MM-dd")+" "+getWeekOfDate(date);
    }

    /**
     * 把long 转换成 日期 再转换成String类型
     */
    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 获取年月日格式yyyy年MM月dd日
     *
     * @param time
     * @return yyyy年MM月dd日
     */
    public static String getYearMouthDay(long time) {
        return transferLongToDate(YY_MM_DD, time* 1000);
    }


    /**
     * 获取年月日格式yyyy-MM-dd HH:mm:ss
     * 2017-08-18 09:37:08
     * @param time
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYearMouthDayHMS(long time) {
        return transferLongToDate(YY_MM_DD_H_M_S, time* 1000);
    }

    public static String getYearMouthDayHM(long time) {
        return transferLongToDate(YY_MM_DD_H_M, time* 1000);
    }



}
