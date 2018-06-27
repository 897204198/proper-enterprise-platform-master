package com.proper.enterprise.platform.oopsearch.api.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;

public class SearchConfigVO extends BaseVO {

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

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 描述
     */
    private String columnDesc;

    /**
     * url
     */
    private String url;

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

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
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

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
