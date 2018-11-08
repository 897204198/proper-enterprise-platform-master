package com.proper.enterprise.platform.core;

import com.proper.enterprise.platform.core.utils.ConfCenter;

import java.nio.charset.Charset;

/**
 * 常量表
 */
public final class PEPConstants {

    public static final String DEFAULT_OPERAOTR_ID = "PEP_SYS";
    /**
     * 默认字符集
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(ConfCenter.get("core.default_charset", "UTF-8"));
    /**
     * 默认年份日期表示格式
     */
    public static final String DEFAULT_YEAR_FORMAT = ConfCenter.get("core.default_year_format", "yyyy");
    /**
     * 默认月份日期表示格式
     */
    public static final String DEFAULT_MONTH_FORMAT = ConfCenter.get("core.default_month_format", "yyyy-MM");
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
     * 响应头中封装的异常编码key
     */
    public static final String RESPONSE_HEADER_ERROR_TYPE = "X-PEP-ERR-TYPE";
    /**
     * 业务异常编码
     */
    public static final String RESPONSE_BUSINESS_ERROR = "PEP_BIZ_ERR";

    /*
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new PEPConstants();
    }

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private PEPConstants() {
    }

}
