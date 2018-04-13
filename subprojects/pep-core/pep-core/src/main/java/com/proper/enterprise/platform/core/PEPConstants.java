package com.proper.enterprise.platform.core;

import com.proper.enterprise.platform.core.utils.ConfCenter;

import java.nio.charset.Charset;

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
    public static final Charset DEFAULT_CHARSET = Charset.forName(ConfCenter.get("core.default_charset", "UTF-8"));

    /**
     * 默认日期表示格式
     */
    public static final String DEFAULT_DATE_FORMAT = ConfCenter.get("core.default_date_format", "yyyy-MM-dd");

    /**
     * 默认日期时间表示格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = ConfCenter.get("core.default_datetime_format", "yyyy-MM-dd HH:mm:ss");

    /**
     * 默认日期时间表示格式，含毫秒
     */
    public static final String DEFAULT_TIMESTAMP_FORMAT = ConfCenter.get("core.default_timestamp_format", "yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 平台版本号，用 6 位 16 进制数表示，如：
     * version 0.1.0 => 0x 00 01 00
     */
    public static final long VERSION = Long.parseLong(ConfCenter.get("core.version", "000000"), 16);

    /**
     * 是否允许 JSON 字段名称没有引号
     */
    public static final boolean ALLOW_UNQUOTED_FIELD_NAMES = Boolean.parseBoolean(ConfCenter.get("core.json.allow_unquoted_field_names"));

    /**
     * 是否允许 JSON 字段名称使用单引号
     */
    public static final boolean ALLOW_SINGLE_QUOTES = Boolean.parseBoolean(ConfCenter.get("core.json.allow_single_quotes"));

    /**
     * 响应头中封装的异常编码key
     */
    public static final String RESPONSE_HEADER_ERROR_TYPE = "X-PEP-ERR-TYPE";

    /**
     * 业务异常编码
     */
    public static final String RESPONSE_BUSINESS_ERROR = "PEP_BIZ_ERR";

    /**
     * 系统异常编码
     */
    public static final String RESPONSE_SYSTEM_ERROR = "PEP_SYS_ERR";

    /**
     * 系统异常信息
     */
    public static final String RESPONSE_SYSTEM_ERROR_MSG = "SYSTEM_ERROR";

}
