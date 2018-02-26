package com.proper.enterprise.platform.search.common.util;

import com.proper.enterprise.platform.core.utils.DateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    public static String addWhereDateRange(String key, String[] value) {
        StringBuffer stringBuffer = new StringBuffer();
        //support date auto compare.
        String date1 = value[0];
        String date2 = value[1];
        Date d1 = DateUtil.toDate(date1);
        Date d2 = DateUtil.toDate(date2);
        String start = date1;
        String end = date2;
        if (d1.getTime() > d2.getTime()) {
            start = date2;
            end = date1;
        }
        stringBuffer.append(key).append(" ").append(">=").append(" ");
        stringBuffer.append("'").append(start).append("'").append(" ");
        stringBuffer.append(" ").append("and").append(" ");
        stringBuffer.append(key).append(" ").append("<=").append(" ");
        stringBuffer.append("'").append(end).append("'").append(" ");
        return stringBuffer.toString();
    }

    public static String addWhereElements(String sql, String logic, String key, String keyType, String operate, String innerlogic, String... value) {
        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append(" ").append(logic).append(" ");
        if (value.length > 1) {
            stringBuffer.append("(");
            if (innerlogic.equals("range")) {
                if (keyType.equals("date")) {
                    stringBuffer.append(addWhereDateRange(key, value));
                }
            } else {
                for (int i = 0; i < value.length; i++) {
                    stringBuffer.append(addElementWithoutLogic("", key, keyType, operate, value[i]));
                    if (i < value.length - 1) {
                        stringBuffer.append(" ").append(innerlogic).append(" ");
                    }
                }
            }
            stringBuffer.append(")");
        } else {
            stringBuffer.append(addElementWithoutLogic("", key, keyType, operate, value[0]));
        }
        return stringBuffer.toString();
    }

    public static String getSeparator(String keyType, String operate, String director) {
        Map<String, String> map = new HashMap<>();
        //op
        map.put("likel", "'%");
        map.put("liker", "%'");
        map.put("=l", "'");
        map.put("=r", "'");
        //type
        map.put("num", "");
        if ("num".equals(keyType)) {
            return map.get(keyType);
        } else {
            return map.get(operate + director);
        }
    }

    public static String addElementWithoutLogic(String sql, String key, String keyType, String operate, String value) {
        operate = operate == null ? getDefaultTypeOperate(keyType) : operate;
        StringBuffer stringBuffer = new StringBuffer(sql);
        stringBuffer.append(" ").append(key).append(" ");
        stringBuffer.append(" ").append(operate).append(" ");
        stringBuffer.append(getSeparator(keyType, operate, "l"));
        stringBuffer.append(value);
        stringBuffer.append(getSeparator(keyType, operate, "r"));
        return stringBuffer.toString();
    }

    public static String getDefaultTypeOperate(String type) {
        Map<String, String> map = new HashMap<>();
        map.put("text", "like");
        map.put("num", "=");
        map.put("date", "like");
        String operate = map.get(type) == null ? "like" : map.get(type);
        return operate;
    }

}
