package com.proper.enterprise.platform.oopsearch.api.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * searchConfig对应的entity
 * */
@Entity
@Table(name = "PEP_OOPSEARCH_CONFIG")
@CacheEntity
public class SearchConfigEntity extends BaseEntity {

    private String moduleName;

    private String tableName;

    private String searchColumn;

    private String columnAlias;

    private String columnType;

    private String columnDesc;

    private String url;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
