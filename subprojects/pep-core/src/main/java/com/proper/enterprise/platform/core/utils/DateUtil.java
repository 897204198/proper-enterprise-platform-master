package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.PEPConstants;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    public static final int DAY = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private DateUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new DateUtil();
    }

    /**
     * 获得当前年份数值表示，例如：2016
     *
     * @return 当前年份数值
     */
    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static Date toDate(String dateStr) {
        return toDate(dateStr, PEPConstants.DEFAULT_DATE_FORMAT);
    }

    public static Date toDateTime(String dateStr) {
        return toDate(dateStr, PEPConstants.DEFAULT_DATETIME_FORMAT);
    }

    public static Date toDate(String dateStr, String format) {
        if(dateStr == null || dateStr.trim().equals("")) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        Date realDate = null;
        try {
            realDate = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realDate;
    }

    public static String toDateString(Date date) {
        return toDateString(date, PEPConstants.DEFAULT_DATE_FORMAT);
    }

    public static String toDateTimeString(Date date) {
        return toDateString(date, PEPConstants.DEFAULT_DATETIME_FORMAT);
    }

    public static String toDateString(Date date, String format) {
        if(date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static String getCurrentDateString() {
        return toDateTimeString(getCurrentDate());
    }

    /**
     * Plus or minus operations to date.
     * For example:
     *
     *     DateUtil.changeDate(DateUtil.getCurrentDate(), DateUtil.DAY, -3)
     *
     * @author Hinex
     * @date 2011-6-13 14:29:42
     * @param dateToChange date to change
     * @param unit         change date with unit
     * @param amount       amount to plus (could be positive and negative
     * @return new date
     */
    public static Date changeDate(Date dateToChange, int unit, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToChange);
        int field = 0;
        switch(unit) {
            case DAY:
                field = Calendar.DAY_OF_MONTH;
                break;
            case MONTH:
                field = Calendar.MONTH;
                break;
            case YEAR:
                field = Calendar.YEAR;
                break;
            default:
                break;
        }
        cal.add(field, amount);
        return cal.getTime();
    }

    /**
     * Use time string to update the time part of date
     *
     * @author Hinex
     * @date 2011-7-6 13:24:19
     * @param date    date time to update
     * @param timeStr time string with format "HH:mm:ss"
     * @return new date
     */
    public Date updateTimePart(Date date, String timeStr) {
        return toDateTime(toDateTimeString(date).replaceFirst("\\d+:\\d+:\\d+", timeStr));
    }

    public static Date getYearMonth1stDate(String year, String month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(year));
        c.set(Calendar.MONTH, Integer.parseInt(month)-1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date getYearMonthLastDate(String year, String month) {
        Date firstDate = getYearMonth1stDate(year, month);
        return changeDate(changeDate(firstDate, MONTH, 1), DAY, -1);
    }

    public static BigDecimal getYearMargin(Date start, Date end) {
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);

        // compute years in this unit
        int endYear = endCal.get(Calendar.YEAR);
        int startYear = startCal.get(Calendar.YEAR);
        int endMonth = endCal.get(Calendar.MONTH);
        int startMonth = startCal.get(Calendar.MONTH);
        int endDay = endCal.get(Calendar.DAY_OF_MONTH);
        int startDay = startCal.get(Calendar.DAY_OF_MONTH);
        int months = (endYear - startYear - (endMonth>=startMonth ? 0 : 1)) * 12;
        months += endMonth>=startMonth ? endMonth-startMonth : 12-(startMonth-endMonth);
        months += endDay>=startDay ? 0 : -1;
        return new BigDecimal(months).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
    }

}
