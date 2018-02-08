package com.proper.enterprise.platform.search.common.util;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.sys.i18n.I18NService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date Time Tools
 * author wanghp
 */
public class DateTimeUtil {

    public static int getTimeAxis(String element, I18NService i18NService) {
        Map<String, Integer> timeAxis = new HashMap<>();
        timeAxis.put(i18NService.getMessage("search.qian"), -2);
        timeAxis.put(i18NService.getMessage("search.zuo"), -1);
        timeAxis.put(i18NService.getMessage("search.shang"), -1);
        timeAxis.put(i18NService.getMessage("search.qu"), -1);
        timeAxis.put(i18NService.getMessage("search.jin"), 0);
        timeAxis.put(i18NService.getMessage("search.ben"), 0);
        timeAxis.put(i18NService.getMessage("search.zhe"), 0);
        timeAxis.put(i18NService.getMessage("search.ming"), 1);
        timeAxis.put(i18NService.getMessage("search.xia"), 1);
        timeAxis.put(i18NService.getMessage("search.hou"), 2);
        return timeAxis.get(element);
    }

    public static String getDateSubject(String element, I18NService i18NService) {
        Map<String, String> dateSubject = new HashMap<>();
        dateSubject.put(i18NService.getMessage("search.day"), "day");
        dateSubject.put(i18NService.getMessage("search.week"), "week");
        dateSubject.put(i18NService.getMessage("search.month"), "month");
        dateSubject.put(i18NService.getMessage("search.quarter"), "quarter");
        dateSubject.put(i18NService.getMessage("search.year"), "year");
        return dateSubject.get(element);
    }

    public static String day(int position) {
        Date newDate = new Date();
        newDate = DateUtil.addDay(newDate, position);
        return DateUtil.toString(newDate, "yyyy-MM-dd");
    }

    public static Map<String, String> weekRange(Integer position) {
        position = position * 7;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.add(Calendar.DATE, position + 1);
        String s = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        cal.add(Calendar.DATE, 6);
        String e = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        Map<String, String> range = new HashMap<>();
        range.put("S", s);
        range.put("E", e);
        return range;
    }

    public static Map<String, String> monthRange(Integer position) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, position);
        String s = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String e = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        Map<String, String> range = new HashMap<>();
        range.put("S", s);
        range.put("E", e);
        return range;
    }

    public static Map<String, String> quarterRange(Integer position) {
        Map<String, String> range = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) / 3 + position) * 3);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String s = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        range.put("S", s);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) / 3 + position) * 3 + 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String e = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        range.put("E", e);
        return range;
    }

    public static Map<String, String> yearRange(Integer position) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, position);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        String s = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        String e = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
        Map<String, String> range = new HashMap<>();
        range.put("S", s);
        range.put("E", e);
        return range;
    }
}
