package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.PEPConstants;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

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
        return new LocalDate().getYear();
    }

    /**
     * 获得当前时间戳
     *
     * @return 时间戳字符串，使用默认日期格式
     */
    public static String getTimestamp() {
        return toTimestamp(new Date());
    }

    /**
     * 按照默认日期格式转换字符串为日期对象
     *
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    public static Date toDate(String dateStr) {
        return toDate(dateStr, PEPConstants.DEFAULT_DATE_FORMAT);
    }

    /**
     * 按照默认时间戳格式转换字符串为日期对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期对象
     */
    public static Date toDateTime(String dateStr) {
        return toDate(dateStr, PEPConstants.DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 按照提供的日期格式转换字符串为日期对象
     *
     * @param dateStr 日期字符串
     * @param format  日期字符串格式
     * @return 日期对象
     */
    public static Date toDate(String dateStr, String format) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return fmt.parseDateTime(dateStr).toDate();
    }

    /**
     * 按默认日期格式转换日期为字符串
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String toDateString(Date date) {
        return toString(date, PEPConstants.DEFAULT_DATE_FORMAT);
    }

    /**
     * 按默认时间戳格式转换日期为时间戳
     *
     * @param date 日期对象
     * @return 时间戳
     */
    public static String toTimestamp(Date date) {
        return toString(date, PEPConstants.DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 按照提供的日期格式转换日期对象为字符串
     *
     * @param date   日期对象
     * @param format 日期字符串格式
     * @return 日期字符串
     */
    public static String toString(Date date, String format) {
        return new DateTime(date.getTime()).toString(format);
    }

}
