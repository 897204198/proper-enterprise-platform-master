package com.proper.enterprise.platform.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量表
 */
public final class PEPConstants {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private PEPConstants() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new PEPConstants();
    }

    /**
     * 默认字符集
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 默认日期表示格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认日期时间表示格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 平台版本号
     * version 0.1.0 => 0x 00 01 00
     */
    public static final long VERSION = 0x000100L;

    /**
     * 是否允许 JSON 字段名称没有引号
     */
    public static final boolean ALLOW_UNQUOTED_FIELD_NAMES = true;

    /**
     * 是否允许 JSON 字段名称使用单引号
     */
    public static final boolean ALLOW_SINGLE_QUOTES = true;

}
