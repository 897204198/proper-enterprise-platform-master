package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;
import java.util.List;

/**
 * 表格页面自定义配置信息(查询条件配置信息)
 */
public class CustomGridConditionDocument implements Serializable {

    /**
     * 条件类别
     */
    private String category;

    /**
     * [或者]条件对应数据表的列
     */
    private List<String> orField;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getOrField() {
        return orField;
    }

    public void setOrField(List<String> orField) {
        this.orField = orField;
    }
}
