package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;

/**
 * 列表页面自定义配置信息(表格数据排序)
 */
public class CustomGridOrderDocument implements Serializable {

    /**
     * 数据键
     */
    private String field;

    /**
     * 排序方式
     */
    private String order;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
