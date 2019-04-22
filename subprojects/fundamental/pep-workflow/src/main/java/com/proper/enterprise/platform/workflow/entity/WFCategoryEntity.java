package com.proper.enterprise.platform.workflow.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@CacheEntity
@Table(name = "PEP_WF_CATEGORY", indexes = {
    @Index(name = "UK_PEP_WF_CATEGORY_NAME", columnList = "name", unique = true),
    @Index(name = "UK_PEP_WF_CATEGORY_CODE", columnList = "code", unique = true)
})
public class WFCategoryEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    /**
     * 类别同级排序号
     */
    @Column(nullable = false)
    private Integer sort;

    /**
     * 描述说明
     */
    private String description;

    /**
     * 父类别
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private WFCategoryEntity parent;

    /**
     * 子类别集合
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private Collection<WFCategoryEntity> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WFCategoryEntity getParent() {
        return parent;
    }

    public void setParent(WFCategoryEntity parent) {
        this.parent = parent;
    }

    public Collection<WFCategoryEntity> getChildren() {
        return children;
    }

    public void setChildren(Collection<WFCategoryEntity> children) {
        this.children = children;
    }

    public void addParent(WFCategoryEntity parent) {
        if (null == parent) {
            this.parent = null;
            return;
        }
        this.parent = parent;
    }

    public void addChild(WFCategoryEntity wfCatagoryEntity) {
        if (null == children) {
            children = new ArrayList<>();
        }
        children.add(wfCatagoryEntity);
    }

    public void removeChild(WFCategoryEntity wfCatagoryEntity) {
        if (null == children) {
            return;
        }
        children.remove(wfCatagoryEntity);
    }
}
