package com.proper.enterprise.platform.core.utils;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.proper.enterprise.platform.core.PEPConstants.*;

/**
 * 日期工具类
 */
public class DateUtil {

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new DateUtil();
    }

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private DateUtil() {
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
     * 按照默认时间戳格式转换字符串为日期对象
     *
     * @param dateStr 日期时间字符串
     * @return 日期对象
     */
    public static Date toDateTime(String dateStr) {
        return toDate(dateStr, DEFAULT_DATETIME_FORMAT);
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
     * @param date        日期对象
     * @param millisecond 是否包含毫秒
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
     * @param date 日期对象
     * @return null 或者克隆出的日期对象
     */
    public static Date safeClone(Date date) {
        return date == null ? null : (Date) date.clone();
    }

    /**
     * 日期添加指定天数
     *
     * @param date 要添加天数的日期,如果为负数，则为减少的天数
     * @param day  添加的天数
     * @return 添加指定分钟数的新的Date对象
     */
    public static Date addDay(Date date, int day) {
        return date == null ? null : new DateTime(date.getTime()).plusDays(day).toDate();
    }

    /**
     * 日期添加指定分钟数
     *
     * @param date   要添加天数的日期,如果为负数，则为减少的分钟数
     * @param minute 添加的天数
     * @return 添加指定分钟数的新的Date对象
     */
    public static Date addMinute(Date date, int minute) {
        return date == null ? null : new DateTime(date.getTime()).plusMinutes(minute).toDate();
    }

    /**
     * 根据时间戳判断是否为时间类型
     *
     * @param dateTimestamp 时间戳
     * @return true是 false否
     */
    public static boolean isDate(String dateTimestamp) {
        String re = "\\d{4}-\\d{2}-\\d{2}(T\\d{2}:\\d{2}:\\d{2}\\.\\d{0,3}Z)?";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(dateTimestamp);
        return m.find();
    }

    /**
     * 处理格林威治时间(零时区)前台的特殊字符T Z 2018-07-23T10:44:05.469Z
     *
     * @param dateTimestamp 时间类型特殊字符
     * @return 时间
     */
    public static Date parseGMTSpecial(String dateTimestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimestamp.length() > 10
            ? PEPConstants.ANT_D_TIMESTAMP_FORMAT
            : PEPConstants.DEFAULT_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT")));
        try {
            return sdf.parse(dateTimestamp);
        } catch (ParseException e) {
            throw new ErrMsgException("time parse error time is " + dateTimestamp);
        }
    }

    /**
     * 日期添加指定星期
     *
     * @param date 要添加星期的日期
     * @param week 添加的星期,如果为负数，则为减少的星期
     * @return 添加指定星期数的新的Date对象
     */
    public static Date addWeek(Date date, int week) {
        return date == null ? null : new DateTime(date.getTime()).plusWeeks(week).toDate();
    }

    /**
     * 获取本周的周几 ，周一、周日为一周的开始和结束
     *
     * @param date      日期
     * @param dayOfWeek 周几 1～7代表周一～周日
     * @return 本周几Date对象
     */
    public static Date getDayOfWeek(Date date, int dayOfWeek) {
        return date == null ? null : new DateTime(date).withDayOfWeek(dayOfWeek).toDate();
    }

    /**
     * 获取年初日期
     *
     * @param date 要获得年初的日期
     * @return 该年度1月1日的Date对象
     */
    public static Date getBeginningOfYear(Date date) {
        return date == null ? null : new DateTime(date).withDayOfYear(1).toDate();
    }
}
