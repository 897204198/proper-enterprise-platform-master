package com.proper.enterprise.platform.core.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

import static com.proper.enterprise.platform.core.PEPConstants.*;

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
     * 获得当前时间戳，不含毫秒
     *
     * @return 时间戳字符串，使用默认时间日期格式
     */
    public static String getTimestamp() {
        return getTimestamp(false);
    }

    /**
     * 获得当前时间戳
     *
     * @param millisecond 是否含毫秒
     * @return 时间戳字符串，传入 true 时可包含毫秒
     */
    public static String getTimestamp(boolean millisecond) {
        return toTimestamp(new Date(), millisecond);
    }

    /**
     * 按照默认日期格式转换字符串为日期对象
     *
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    public static Date toDate(String dateStr) {
        return toDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 按照默认时间戳格式转换字符串为日期对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期对象
     */
    public static Date toDateTime(String dateStr) {
        return toDate(dateStr, DEFAULT_DATETIME_FORMAT);
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
        return fmt.parseLocalDateTime(dateStr).toDate();
    }

    /**
     * 按默认日期格式转换日期为字符串
     *
     * @param date 日期对象
     * @return 日期字符串
     */
    public static String toDateString(Date date) {
        return toString(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 按默认时间戳格式转换日期为时间戳，不含毫秒
     *
     * @param date 日期对象
     * @return 时间戳
     */
    public static String toTimestamp(Date date) {
        return toTimestamp(date, false);
    }

    /**
     * 按默认时间戳格式转换日期为时间戳
     *
     * @param date          日期对象
     * @param millisecond   是否包含毫秒
     * @return 时间戳
     */
    public static String toTimestamp(Date date, boolean millisecond) {
        return toString(date, millisecond ? DEFAULT_TIMESTAMP_FORMAT : DEFAULT_DATETIME_FORMAT);
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

    /**
     * 安全 clone 日期对象
     * 常用在 Java Bean 中的 Date 类型属性的 getter 和 setter 方法中
     * 防止将 Bean 中的私有属性直接暴露出来
     *
     * @param date  日期对象
     * @return null 或者克隆出的日期对象
     */
    public static Date safeClone(Date date) {
        return date == null ? null : (Date) date.clone();
    }

    /**
     * 日期添加指定天数
     *
     * @param date 要添加天数的日期,如果为负数，则为减少的天数
     * @param day 添加的天数
     * @return 添加指定分钟数的新的Date对象
     */
    public static Date addDay(Date date, int day) {
        return date == null ? null: new DateTime(date.getTime()).plusDays(day).toDate();
    }
    /**
     * 日期添加指定分钟数
     *
     * @param date 要添加天数的日期,如果为负数，则为减少的分钟数
     * @param minute 添加的天数
     * @return 添加指定分钟数的新的Date对象
     */
    public static Date addMinute(Date date, int minute) {
        return date == null ? null : new DateTime(date.getTime()).plusMinutes(minute).toDate();
    }

}
