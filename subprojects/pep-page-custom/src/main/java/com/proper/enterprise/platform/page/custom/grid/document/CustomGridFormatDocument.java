package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;

/**
 * 表格页面自定义配置信息(表格列格式化)
 */
public class CustomGridFormatDocument implements Serializable {

    /**
     * 格式化方式
     */
    private String type;

    /**
     * 值映射基础数据
     */
    private String dataName;

    /**
     * 值映射基础数据键
     */
    private String code;

    /**
     * 值映射基础数据值
     */
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
