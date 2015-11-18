package com.proper.enterprise.platform.core.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {
    
    private StringUtil() { }

    public static boolean isNull(String str) {
        return isBlank(str);
    }

    public static boolean isNotNull(String str) {
        return isNotBlank(str);
    }

    public static String cleanUrl(String url) {
        if (isNull(url)) {
            return "";
        }

        int idx = url.indexOf("?");
        return idx > 0 ? url.substring(0, idx) : url;
    }

    public static String validStr(Object obj) {
        if (obj==null || !(obj instanceof String)) {
            return "";
        } else {
            return ((String)obj).trim();
        }
    }
    
    /**
     * add single quote to string's boundary and escape ' to ''
     * @author Hinex
     * @date 2011-8-22 12:55:48
     * @param s
     * @return
     */
    public static String singleQuoteAndEscape(String s) {
        if (s != null) {
            StringBuffer t = new StringBuffer(s.length() + 3);
            t.append("'");
            t.append(s.replaceAll("'", "''"));
            t.append("'");
            return t.toString();
        }
        return "''";
    }
    
    public static String delLastComma(String str) {
        if (isNull(str)) {
            return "";
        }
        if (str.endsWith(",")) {
            return delLastChar(str);
        } else {
            return str;
        }
    }
    
    public static String delLastChar(String str) {
        if (isNull(str)||str.length()<2) {
            return "";
        }
        return str.substring(0, str.length()-1);
    }
    
    public static String getGetterMethodName(String attributeName) {
        return "get" + upperFirstChar(attributeName);
    }
    
    public static String getSetterMethodName(String attributeName) {
        return "set" + upperFirstChar(attributeName);
    }
    
    public static String upperFirstChar(String str) {
        if (isNull(str)) {
            return "";
        }
        
        if (str.trim().length() == 1) {
            return str.toUpperCase();
        }
        
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    
    public static String getNameFromGetter(String getterName) {
        return lowerFirstChar(getterName.replace("get", ""));
    }
    
    public static String getNameFromSetter(String setterName) {
        return lowerFirstChar(setterName.replace("set", ""));
    }
    
    public static String lowerFirstChar(String str) {
        if (isNull(str)) {
            return "";
        }
        
        if (str.trim().length() == 1) {
            return str.toLowerCase();
        }
        
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
    
}
