package com.proper.enterprise.platform.app.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.Map;

public class ApplicationVO extends BaseVO {

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }

    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用跳转页面
     */
    private String page;
    /**
     * 应用图标
     */
    private String icon;
    /**
     * 跳转页面类型
     */
    private String style;
    /**
     * 应用页面初始化参数
     */
    private Map data;
    /**
     * 应用类别编码
     */
    private String code;
    /**
     * 是否为默认值
     */
    private Boolean defaultValue;

    /**
     * 角标数
     */
    private String msgCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }
}
