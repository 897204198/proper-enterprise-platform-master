package com.proper.enterprise.platform.page.custom.grid.document;

import java.io.Serializable;

/**
 * 表格页面自定义配置信息(基础数据)
 */
public class CustomGridDataDocument implements Serializable {

    /**
     * 变量名称
     */
    private String name;

    /**
     * 取得地址
     */
    private String templateUrl;

    /**
     * json字符串
     */
    private String template;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
