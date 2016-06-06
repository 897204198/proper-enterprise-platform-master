package com.proper.enterprise.platform.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 常量表
 */
public abstract class PEPConstants {

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

}
