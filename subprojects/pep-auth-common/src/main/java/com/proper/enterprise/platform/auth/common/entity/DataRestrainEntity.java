package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PEP_AUTH_DATARESTRAINS")
@CacheEntity
public class DataRestrainEntity extends BaseEntity implements DataRestrain {

    private String tableName;

    private String sql;

    private String filterName;

    @Column(unique = true)
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

}
