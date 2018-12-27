package com.proper.enterprise.platform.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.core")
public class CoreProperties {

    /**
     * 字符集
     */
    private String charset = "UTF-8";

    /**
     * 是否允许 JSON 字段名称没有引号
     */
    private boolean allowUnquotedFieldNames = true;

    /**
     * 是否允许 JSON 字段名称使用单引号
     */
    private boolean allowSingleQuotes = true;

    /**
     * 前台的时间表示格式
     */
    private String antdTimestampFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * 默认日期表示格式
     */
    private String defaultDateFormat = "yyyy-MM-dd";

    /**
     * 默认年份日期表示格式
     */
    private String defaultYearFormat = "yyyy";

    /**
     * 默认月份日期表示格式
     */
    private String defaultMonthFormat = "yyyy-MM";

    /**
     * 默认日期时间表示格式
     */
    private String defaultDatetimeFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期时间表示格式，含毫秒
     */
    private String defaultTimestampFormat = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 系统默认操作人Id
     */
    private String defaultOperatorId = "PEP_SYS";

    /**
     * ASE配置Mode
     */
    private String secretAesMode = "ECB";

    /**
     * ASE配置Padding
     */
    private String secretAesPadding = "PKCS5Padding";

    /**
     * ASE配置Key
     */
    private String secretAesKey = "CAFEBABEEBABEFAC";

    /**
     * gzip忽略文件路径
     */
    private String gzipExcludePathPatterns;

    /**
     * 搜集平台统计信息站点地址
     */
    private String statSite = "https://cloud.propersoft.cn/requesturl";

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isAllowUnquotedFieldNames() {
        return allowUnquotedFieldNames;
    }

    public void setAllowUnquotedFieldNames(boolean allowUnquotedFieldNames) {
        this.allowUnquotedFieldNames = allowUnquotedFieldNames;
    }

    public boolean isAllowSingleQuotes() {
        return allowSingleQuotes;
    }

    public void setAllowSingleQuotes(boolean allowSingleQuotes) {
        this.allowSingleQuotes = allowSingleQuotes;
    }

    public String getAntdTimestampFormat() {
        return antdTimestampFormat;
    }

    public void setAntdTimestampFormat(String antdTimestampFormat) {
        this.antdTimestampFormat = antdTimestampFormat;
    }

    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    public String getDefaultYearFormat() {
        return defaultYearFormat;
    }

    public void setDefaultYearFormat(String defaultYearFormat) {
        this.defaultYearFormat = defaultYearFormat;
    }

    public String getDefaultMonthFormat() {
        return defaultMonthFormat;
    }

    public void setDefaultMonthFormat(String defaultMonthFormat) {
        this.defaultMonthFormat = defaultMonthFormat;
    }

    public String getDefaultDatetimeFormat() {
        return defaultDatetimeFormat;
    }

    public void setDefaultDatetimeFormat(String defaultDatetimeFormat) {
        this.defaultDatetimeFormat = defaultDatetimeFormat;
    }

    public String getDefaultTimestampFormat() {
        return defaultTimestampFormat;
    }

    public void setDefaultTimestampFormat(String defaultTimestampFormat) {
        this.defaultTimestampFormat = defaultTimestampFormat;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public void setDefaultOperatorId(String defaultOperatorId) {
        this.defaultOperatorId = defaultOperatorId;
    }

    public String getSecretAesMode() {
        return secretAesMode;
    }

    public void setSecretAesMode(String secretAesMode) {
        this.secretAesMode = secretAesMode;
    }

    public String getSecretAesPadding() {
        return secretAesPadding;
    }

    public void setSecretAesPadding(String secretAesPadding) {
        this.secretAesPadding = secretAesPadding;
    }

    public String getSecretAesKey() {
        return secretAesKey;
    }

    public void setSecretAesKey(String secretAesKey) {
        this.secretAesKey = secretAesKey;
    }

    public String getGzipExcludePathPatterns() {
        return gzipExcludePathPatterns;
    }

    public void setGzipExcludePathPatterns(String gzipExcludePathPatterns) {
        this.gzipExcludePathPatterns = gzipExcludePathPatterns;
    }

    public String getStatSite() {
        return statSite;
    }

    public void setStatSite(String statSite) {
        this.statSite = statSite;
    }
}
