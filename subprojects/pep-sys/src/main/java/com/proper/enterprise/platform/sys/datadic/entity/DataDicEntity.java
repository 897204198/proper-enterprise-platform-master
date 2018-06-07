package com.proper.enterprise.platform.sys.datadic.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "PEP_SYS_DATA_DIC",
    uniqueConstraints = {@UniqueConstraint(name = "UK_CATALOG_CODE", columnNames = {"DD_CATALOG", "DD_CODE"})})
@CacheEntity
public class DataDicEntity extends BaseEntity implements DataDic {

    @Column(nullable = false, name = "DD_CATALOG")
    private String catalog;
    @Column(nullable = false, name = "DD_CODE")
    private String code;
    @Column(nullable = false, name = "DD_NAME")
    private String name;
    @Column(nullable = false, name = "DD_ORDER", columnDefinition = "int(5) DEFAULT 1")
    private int order;
    @Column(nullable = false, name = "DD_TYPE", columnDefinition = "VARCHAR(8) DEFAULT 'SYSTEM'")
    @Enumerated(EnumType.STRING)
    private DataDicTypeEnum dataDicType;
    /**
     * 是否为默认项
     */
    @Type(type = "yes_no")
    @Column(nullable = false, name = "IS_DEFAULT", columnDefinition = "CHAR(1) DEFAULT 'N'")
    protected Boolean deft;

    public DataDicEntity() {
    }

    public DataDicEntity(String catalog, String code, String name, int order) {
        this.catalog = catalog;
        this.code = code;
        this.name = name;
        this.order = order;
    }

    @Override
    public String toString() {
        return catalog + DD_CATALOG_CODE_SEPARATOR + code + DD_CATALOG_CODE_SEPARATOR + name;
    }

    @Override
    public int hashCode() {
        return StringUtil.defaultString(catalog).hashCode() + StringUtil.defaultString(code).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DataDic) {
            DataDic dd = (DataDic) obj;
            return StringUtil.equals(catalog, dd.getCatalog()) && StringUtil.equals(code, dd.getCode()) && StringUtil.equals(name, dd.getName());
        }
        if (obj instanceof DataDicLite) {
            DataDicLite lite = (DataDicLite) obj;
            return StringUtil.equals(catalog, lite.getCatalog()) && StringUtil.equals(code, lite.getCode());
        }
        return false;
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    @Override
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }


    public Boolean getDeft() {
        return deft;
    }

    public void setDeft(Boolean deft) {
        this.deft = deft;
    }

    public DataDicTypeEnum getDataDicType() {
        return dataDicType;
    }

    public void setDataDicType(DataDicTypeEnum dataDicType) {
        this.dataDicType = dataDicType;
    }
}
