package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.DataRestrain;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_DATARESTRAINS")
@CacheEntity
public class DataRestrainEntity extends BaseEntity implements DataRestrain {

    private String tableName;

    private String sqlStr;

    private String filterName;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "dataRestrainEntities")
    private Collection<ResourceEntity> resourceEntities = new ArrayList<>();

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

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sql) {
        this.sqlStr = sql;
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
