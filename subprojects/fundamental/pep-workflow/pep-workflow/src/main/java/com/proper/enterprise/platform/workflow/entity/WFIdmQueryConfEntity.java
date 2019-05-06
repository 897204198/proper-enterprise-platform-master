package com.proper.enterprise.platform.workflow.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 流程关于权限查询的扩展配置表
 * 每个对于权限的扩展必须实现PEPCandidateExtQuery
 * 保留实现PEPCandidateExtQuery的bean名称并配置在此表中
 * 扩展类型全局唯一
 */
@Entity
@CacheEntity
@Table(name = "PEP_WF_IDM_QUERY_CONF")
public class WFIdmQueryConfEntity extends BaseEntity {

    /**
     * 扩展类型
     */
    @Column(nullable = false, unique = true)
    private String type;

    /**
     * 扩展类型名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 实现的bean名称
     */
    @Column(nullable = false)
    private String beanName;

    /**
     * 排序
     */
    @Column(nullable = false)
    private Integer sort;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
