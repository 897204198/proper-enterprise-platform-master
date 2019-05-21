package com.proper.enterprise.platform.workflow.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@CacheEntity
@Table(name = "PEP_WF_IDM_RULE_CONF", indexes = @Index(name = "UK_PEP_WF_IDM_RULE_CONF_RULE", unique = true, columnList = "rule"))
public class WFIdmRuleConfEntity extends BaseEntity {

    /**
     * 规则名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 规则
     */
    @Column(nullable = false)
    private String rule;

    /**
     * 规则类型
     */
    @Column(nullable = false)
    private String type;

    /**
     * 排序
     */
    @Column(nullable = false, length = 10)
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
