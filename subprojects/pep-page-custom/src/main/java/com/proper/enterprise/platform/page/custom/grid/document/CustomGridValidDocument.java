package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;

/**
 * 列表页面自定义配置信息(表单元素验证)
 */
public class CustomGridValidDocument implements Serializable {

    /**
     * 验证方式
     */
    private String validMode;

    public String getValidMode() {
        return validMode;
    }

    public void setValidMode(String validMode) {
        this.validMode = validMode;
    }
}
