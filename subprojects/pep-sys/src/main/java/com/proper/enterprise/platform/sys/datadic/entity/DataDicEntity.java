package com.proper.enterprise.platform.sys.datadic.entity;

import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.DataDicLite;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_SYS_DATA_DIC")
@CacheEntity
public class DataDicEntity extends BaseEntity implements DataDic {

    @Column(name = "DD_CATALOG")
    private String catalog;
    @Column(name = "DD_CODE")
    private String code;
    @Column(name = "DD_NAME")
    private String name;
    @Column(name = "DD_ORDER")
    private int order;

    /**
     * 是否为默认项
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    protected boolean isDefault;

    public DataDicEntity() { }

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

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean dft) {
        this.isDefault = dft;
    }

}
