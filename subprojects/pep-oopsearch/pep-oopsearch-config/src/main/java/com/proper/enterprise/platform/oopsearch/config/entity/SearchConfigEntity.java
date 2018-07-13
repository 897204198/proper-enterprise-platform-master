package com.proper.enterprise.platform.oopsearch.config.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;

import javax.persistence.*;


/**
 * searchConfig对应的entity
 */
@Entity
@Table(name = "PEP_OOPSEARCH_CONFIG", indexes = {
    @Index(name = "UK_TABLE_NAME_SEARCH_COLUMN_URL", columnList = "tableName, searchColumn, url", unique = true),
    @Index(name = "UK_TABLE_NAME_COLUMN_ALIAS_URL", columnList = "tableName, columnAlias, url", unique = true)})
@CacheEntity
public class SearchConfigEntity extends BaseEntity {

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 字段名称
     */
    private String searchColumn;

    /**
     * 别名
     */
    private String columnAlias;

    private String columnType;

    /**
     * 描述
     */
    private String columnDesc;

    /**
     * url
     */
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'RDB'")
    private DataBaseType dataBaseType;

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

    public DataBaseType getDataBaseType() {
        return dataBaseType;
    }

    public void setDataBaseType(DataBaseType dataBaseType) {
        this.dataBaseType = dataBaseType;
    }
}
