package com.ninethree.palychannelbusiness.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtil {

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param milliseconds
     * @return
     */
    public static String parseLongDate(Long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date(milliseconds);

        return format.format(date);

    }

    /**
     * 格式化日期 yyyy-MM-dd
     *
     * @param milliseconds
     * @return
     */
    public static String parseLongDate2(Long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date date = new Date(milliseconds);

        return format.format(date);

    }

    /**
     * String转化为Date
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
                Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date stringToDate(String dateString, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());// 设置日期格式
        return format.format(date);
    }

    public static String formatDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd",
                Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    public static String formatDateTimeString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
                Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    /**
     * 字符串格式日期比较
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return -1:开始日期小 ;0:日期相等;1:开始日期大
     * @throws ParseException
     */
    public static int compareDate(String startDate, String endDate,
                                  String pattern) throws ParseException {
        Date sDate = stringToDate(startDate, pattern);
        Date eDate = stringToDate(endDate, pattern);
        int result = sDate.compareTo(eDate);
        return result;
    }

    /**
     * 字符串格式日期比较
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return -1:开始日期小 ;0:日期相等;1:开始日期大
     */
    public static int compareDate(String startDate, String endDate) {
        Date sDate = stringToDate(startDate, "yyyy-MM-dd");
        Date eDate = stringToDate(endDate, "yyyy-MM-dd");
        int result = sDate.compareTo(eDate);
        return result;
    }

    /**
     * @return String
     * @throws
     * @Title: getDate
     * @Description:获取当前时间
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
                Locale.getDefault());// 设置日期格式
        return format.format(new Date());
    }

    /**
     * @param pattern 格式
     * @return String
     * @throws
     * @Title: getDate
     * @Description: 获取当前时间
     * @author user
     */
    public static String getDate(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern,
                Locale.getDefault());// 设置日期格式
        return format.format(new Date());
    }

    //获取本周第一天
    public static String getWeekFirstDate() {
        Calendar cal = Calendar.getInstance();

        if(cal.get(Calendar.DAY_OF_WEEK) == 1){
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return dateToString(cal.getTime());
    }

    //获取本月第一天
    public static String getMonthFirstDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateToString(cal.getTime());
    }

    //获取本年第一天
    public static String getYearFirstDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateToString(cal.getTime());
    }

    // 获取当前月第一天：
    public static String getFirstDate(Integer year, Integer month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        // 设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(c.getTime());
    }

    // 获取当前月最后一天：
    public static String getLastDate(Integer year, Integer month) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(c.getTime());
    }

    public static Date getTomorrowDate() {
        // 获取当前日期
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        String nowDate = sf.format(date);
        // System.out.println(nowDate);
        // 通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf.parse(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DAY_OF_YEAR, +1);
        // String nextDate_1 = sf.format(cal.getTime());
        // System.out.println(nextDate_1);
        return cal.getTime();

        // 通过秒获取下一天日期
        // long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒
        // date.setTime(time * 1000);//毫秒
        // String nextDate_2 = sf.format(date).toString();
        // System.out.println(nextDate_2);
    }

    /**
     * 获取N天之后的日期
     * @param n
     * @return
     */
    public static String afterNDay(int n){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        Date d = c.getTime();
        return sf.format(d);
    }

    /**
     * @param dateStr1 开始日期
     * @param dateStr2 结束日期
     * @return int 月数
     * @Title: calculateMonthIn
     * @Description: 计算两个日期相差的月数
     */
    public static int calculateMonthIn(String dateStr1, String dateStr2) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());

        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        try {
            cal1.setTime(sdf.parse(dateStr1));
            cal2.setTime(sdf.parse(dateStr2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int c = (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
                + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);

        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        int result = day2 - day1;

        if (result > 15) {
            c++;
        }
        if (result < -15) {
            c--;
        }

        return c;
    }
}
