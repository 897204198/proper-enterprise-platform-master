package com.proper.enterprise.platform.oopsearch.api.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * mongodb中文档对应的entity
 * */
@Document(collection = "search_column")
public class SearchDocument implements OOPSearchDocument {

    // 内容
    @Indexed
    private String con;

    // 字段
    private String col;

    // 表名
    private String tab;

    // 描述
    private String des;

    // 主键内容拼接字符串
    private String pri;

    /**
     * 获取内容
     * @return 内容
     * */
    public String getCon() {
        return con;
    }

    /**
     * 赋值内容
     * @param  con 内容
     * */
    public void setCon(String con) {
        this.con = con;
    }

    /**
     * 获取字段
     * @return 字段
     * */
    public String getCol() {
        return col;
    }

    /**
     * 赋值字段
     * @param  col 字段
     * */
    public void setCol(String col) {
        this.col = col;
    }

    /**
     * 获取表名
     * @return 表名
     * */
    public String getTab() {
        return tab;
    }

    /**
     * 赋值表名
     * @param  tab 表名
     * */
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     * 获取描述
     * @return 描述
     * */
    public String getDes() {
        return des;
    }

    /**
     * 赋值描述
     * @param  des 描述
     * */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * 获取主键内容拼接字符串
     * @return 主键内容拼接字符串
     * */
    public String getPri() {
        return pri;
    }

    /**
     * 赋值主键内容拼接字符串
     * @param  pri 主键内容拼接字符串
     * */
    public void setPri(String pri) {
        this.pri = pri;
    }
}
