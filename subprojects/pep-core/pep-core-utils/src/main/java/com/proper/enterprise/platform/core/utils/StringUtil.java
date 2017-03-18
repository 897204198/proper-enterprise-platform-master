package com.proper.enterprise.platform.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * 直接继承自 apache commons-lang 中的字符串工具类，并扩展一些方法
 */
public class StringUtil extends StringUtils {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private StringUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new StringUtil();
    }

    public static boolean isNull(String str) {
        return isBlank(str);
    }

    public static boolean isNotNull(String str) {
        return isNotBlank(str);
    }

    public static String camelToSnake(String camel) {
        String[] strs = StringUtil.splitByCharacterTypeCamelCase(camel);
        return StringUtil.join(strs, "_").toLowerCase();
    }

}
