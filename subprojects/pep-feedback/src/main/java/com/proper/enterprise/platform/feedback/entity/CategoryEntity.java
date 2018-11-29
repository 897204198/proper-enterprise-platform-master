package com.proper.enterprise.platform.feedback.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_PROBLEM_CATEGORY")
public class CategoryEntity extends BaseEntity {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 跳转页
     */
    private String pageUrl;

    /**
     * 排序字段
     */
    private String sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
