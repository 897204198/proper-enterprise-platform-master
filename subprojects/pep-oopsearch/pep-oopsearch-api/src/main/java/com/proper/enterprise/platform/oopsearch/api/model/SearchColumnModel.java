package com.proper.enterprise.platform.oopsearch.api.model;

import java.io.Serializable;

/**
 * 查询字段对象模型
 * 用于不同模块，对查询字段对象的获取、转换等操作
 * */
public class SearchColumnModel implements Serializable {

    /**
     * 字段
     */
    private String column;

    /**
     * 表名
     */
    private String table;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段描述
     */
    private String descColumn;

    /**
     * 字段别名
     */
    private String columnAlias;

    /**
     * 请求url
     */
    private String url;


    /**
     * 获取字段
     * @return 字段
     * */
    public String getColumn() {
        return column;
    }

    /**
     * 赋值字段
     * @param  column 字段
     * */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * 获取表名
     * @return 表名
     * */
    public String getTable() {
        return table;
    }

    /**
     * 赋值表名
     * @param  table 表名
     * */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * 获取字段类型
     * @return 字段类型
     * */
    public String getType() {
        return type;
    }

    /**
     * 赋值字段类型
     * @param  type 字段类型
     * */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取字段描述
     * @return 字段描述
     * */
    public String getDescColumn() {
        return descColumn;
    }

    /**
     * 赋值字段描述
     * @param  descColumn 字段描述
     * */
    public void setDescColumn(String descColumn) {
        this.descColumn = descColumn;
    }

    /**
     * 获取字段别名
     * @return 字段别名
     * */
    public String getColumnAlias() {
        return columnAlias;
    }

    /**
     * 赋值字段别名
     * @param  columnAlias 字段别名
     * */
    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    /**
     * 获取访问路径
     * @return 访问路径
     * */
    public String getUrl() {
        return url;
    }

    /**
     * 赋值访问路径
     * @param  url 访问路径
     * */
    public void setUrl(String url) {
        this.url = url;
    }
}
