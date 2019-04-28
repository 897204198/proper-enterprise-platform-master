package com.proper.enterprise.platform.sys.datadic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "PEP_SYS_DATA_DIC_CATALOG")
@CacheEntity
public class DataDicCatalogEntity extends BaseEntity {

    @Column(nullable = false, name = "DD_CATALOG_CODE", unique = true)
    private String catalogCode;

    @Column(nullable = false, name = "DD_CATALOG_NAME")
    private String catalogName;

    @Column(nullable = false, name = "DD_CATALOG_TYPE", columnDefinition = "VARCHAR(8) DEFAULT 'SYSTEM'")
    @Enumerated(EnumType.STRING)
    private DataDicTypeEnum catalogType;

    @Column(nullable = false, name = "DD_CATALOG_ORDER", columnDefinition = "int(10) DEFAULT 1")
    private Integer sort;

    /**
     * 父菜单
     */
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private DataDicCatalogEntity parent;

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public DataDicTypeEnum getCatalogType() {
        return catalogType;
    }

    public void setCatalogType(DataDicTypeEnum catalogType) {
        this.catalogType = catalogType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public DataDicCatalogEntity getParent() {
        return parent;
    }

    public void setParent(DataDicCatalogEntity parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
