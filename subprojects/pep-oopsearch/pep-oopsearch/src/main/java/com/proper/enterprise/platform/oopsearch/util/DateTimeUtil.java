package com.proper.enterprise.platform.oopsearch.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;

import java.util.*;

/**
 * Date Time Tools
 * author wanghp
 */
public class DateTimeUtil {

    public static int getTimeAxis(String element) {
        Map<String, Integer> timeAxis = new HashMap<>();
        List<DataDic> collection = (List<DataDic>)PEPApplicationContext.getBean(DataDicService.class).findByCatalog("TIME_AXIS");
        for (DataDic temp : collection) {
            timeAxis.put(temp.getName(), Integer.parseInt(temp.getCode()));
        }
        return timeAxis.get(element);
    }

    public static String getDateSubject(String element) {
        Map<String, String> dateSubject = new HashMap<>();
        List<DataDic> collection = (List<DataDic>)PEPApplicationContext.getBean(DataDicService.class).findByCatalog("DATE_TYPE");
        for (DataDic temp : collection) {
            dateSubject.put(temp.getName(), temp.getCode());
        }
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
