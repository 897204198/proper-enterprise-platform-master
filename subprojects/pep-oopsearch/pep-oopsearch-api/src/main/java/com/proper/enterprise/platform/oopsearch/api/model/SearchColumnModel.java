package com.proper.enterprise.platform.oopsearch.api.model;

import java.io.Serializable;

/**
 * 查询字段对象模型
 * 用于不同模块，对查询字段对象的获取、转换等操作
 * */
public class SearchColumnModel implements Serializable {

    // 字段
    private String column;

    // 表名
    private String table;

    // 字段类型
    private String type;

    // 字段描述
    private String descColumn;

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
}
