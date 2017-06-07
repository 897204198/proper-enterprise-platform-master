package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;

/**
 * 表格页面自定义配置信息(列配置信息)
 */
public class CustomGridColumnDocument implements Serializable {

    /**
     * 数据键
     */
    private String field;

    /**
     * 列名称
     */
    private String title;

    /**
     * 对齐方式
     */
    private String align;

    /**
     * 可显示字符数
     */
    private String displayCharNum;

    /**
     * 列格式化
     */
    private CustomGridFormatDocument formatter;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getDisplayCharNum() {
        return displayCharNum;
    }

    public void setDisplayCharNum(String displayCharNum) {
        this.displayCharNum = displayCharNum;
    }

    public CustomGridFormatDocument getFormatter() {
        return formatter;
    }

    public void setFormatter(CustomGridFormatDocument formatter) {
        this.formatter = formatter;
    }
}
