package com.proper.enterprise.platform.sys.bizrule.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PEP_SYS_RULES")
@CacheEntity
public class RuleEntity extends BaseEntity {

    /**
     * 规则描述，使用 Spring EL 表达式描述
     */
    @Column(nullable = false, length = 4000)
    private String rule;

    /**
     * 规则名称
     */
    @Column(name = "RULE_NAME")
    private String name;

    /**
     * 备注.
     */
    @Column(name = "RULE_DESCRIPTION", length = 4000)
    private String description;

    /**
     * 使用说明.
     */
    @Column(name = "RULE_HOWTOUSE", length = 4000)
    private String howToUse;

    @Transient
    private String lastModifyUserName;

    /**
     * 规则分类
     */
    private String catalogue;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
    }

    @JsonProperty("lastModifyUserId")
    @Override
    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    @JsonProperty("lastModifyTime")
    @Override
    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public String getLastModifyUserName() {
        return lastModifyUserName;
    }

    public void setLastModifyUserName(String lastModifyUserName) {
        this.lastModifyUserName = lastModifyUserName;
    }

}
