package com.proper.enterprise.platform.sys.datadic.entity;

import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.sys.datadic.DataDic;

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

    public UserEntity() { }

    public DataDicEntity(String catalog, String code, String name, int order) {
        this.catalog = catalog;
        this.code = code;
        this.name = name;
        this.order = order;
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

}
