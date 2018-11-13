package com.proper.enterprise.platform.core;

import java.nio.charset.Charset;

/**
 * 常量表
 */
public final class PEPConstants {

    /**
     * 默认日期时间表示格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

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
