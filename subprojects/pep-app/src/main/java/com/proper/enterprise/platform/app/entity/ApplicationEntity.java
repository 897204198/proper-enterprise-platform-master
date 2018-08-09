package com.proper.enterprise.platform.app.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PEP_APP_APPLICATIONS")
public class ApplicationEntity extends BaseEntity implements Serializable {

    /**
     * 应用名称
     */
    @Column
    private String name;
    /**
     * 应用跳转页面
     */
    @Column
    private String page;
    /**
     * 应用图标
     */
    @Column
    private String icon;
    /**
     * 跳转页面类型
     */
    @Column
    private String style;
    /**
     * 应用页面初始化参数
     */
    @Column
    private String data;
    /**
     * 应用类别编码
     */
    @Column
    private String code;

    @Column
    private Boolean defaultValue;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
