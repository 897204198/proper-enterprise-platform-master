package com.proper.enterprise.platform.notice.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.converter.DataDicLiteConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_NOTICE_TEMPLATE")
@CacheEntity
public class TemplateEntity extends BaseEntity {

    /**
     * 标识
     */
    @Column(nullable = false)
    private String code;

    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 标题
     */
    @Column(nullable = false)
    private String title;

    /**
     * 模板
     */
    @Column(nullable = false)
    private String template;

    /**
     * 目录
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite catelog;

    /**
     * 类型
     */
    @Convert(converter = DataDicLiteConverter.class)
    private DataDicLite type;

    /**
     * 解释
     */
    private String description;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public DataDicLite getCatelog() {
        return catelog;
    }

    public void setCatelog(DataDicLite catelog) {
        this.catelog = catelog;
    }

    public DataDicLite getType() {
        return type;
    }

    public void setType(DataDicLite type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
