package com.proper.enterprise.platform.search.common.util;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;

import java.util.Date;
import java.util.Set;

/**
 * Date Time Tools
 * author wanghp
 */
public class SqlInstallUtil {

    public static String addSelectElements(String sql, Set<String> value) {
        StringBuffer stringBuffer = new StringBuffer("select ");
        for (String str : value) {
            stringBuffer.append(" ").append(str);
            stringBuffer.append(" ,");
        }
        stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public static String addTableElements(String sql, String value) {
        return sql + " from " + value + " where 1=1";
    }

    public static String addRangeElements(String sql, String key, String date1, String date2) {
        Date d1 = DateUtil.toDate(date1);
        Date d2 = DateUtil.toDate(date2);
        String start = date1;
        String end = date2;
        if (d1.getTime() > d2.getTime()) {
            start = date2;
            end = date1;
        }
        StringBuffer stringBuffer = new StringBuffer(sql + "(");
        stringBuffer.append(key).append(" ").append(">=").append(" ");
        stringBuffer.append("'").append(start).append("'").append(" ");
        stringBuffer.append("and").append(" ");
        stringBuffer.append(key).append(" ").append("<=").append(" ");
        stringBuffer.append("'").append(end).append("'").append(" ");
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public static String addWhereElement(String sql, String logic, String key, String operate, String value) {
        StringBuffer stringBuffer = new StringBuffer(sql + " ");
        stringBuffer.append(logic);
        stringBuffer.append(addElementWithoutLogic("", key, operate, value));
        return stringBuffer.toString();
    }

    public static String addWhereElementInt(String sql, String logic, String key, String operate, String value) {
        StringBuffer stringBuffer = new StringBuffer(sql + " ");
        stringBuffer.append(logic);
        stringBuffer.append(" ").append(key).append(" ");
        stringBuffer.append(operate).append(" ");
        stringBuffer.append(value).append(" ");
        return stringBuffer.toString();
    }

    public static String addWhereElements(String sql, String key, String operate, String[] value) {
        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append("(");
        for (int i = 0; i < value.length; i++) {
            stringBuffer.append(addElementWithoutLogic("", key, operate, value[i]));
            if (i < value.length - 1) {
                stringBuffer.append(" or ");
            }
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public static String addElementWithoutLogic(String sql, String key, String operate, String value) {
        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append(" ").append(key).append(" ");
        if (!StringUtil.trim(operate).equals("")) {
            stringBuffer.append(operate).append(" ");
            if (operate.contains("like")) {
                stringBuffer.append("'%").append(value).append("%'");
            } else {
                stringBuffer.append("'").append(value).append("'");
            }
        }
        return stringBuffer.toString();
    }

}
