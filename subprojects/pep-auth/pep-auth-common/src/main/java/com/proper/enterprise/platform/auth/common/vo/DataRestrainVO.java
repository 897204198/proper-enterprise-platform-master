package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.DataRestrain;

import javax.persistence.Column;

public class DataRestrainVO extends BaseVO implements DataRestrain {

    private String tableName;

    private String sqlStr;

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

    @Override
    public String getSqlStr() {
        return sqlStr;
    }

    @Override
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
