package com.proper.enterprise.platform.oopsearch.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * mongodb中文档对应的entity
 * */
@Document(collection = "search_column")
public class SearchDocument extends BaseDocument implements OOPSearchDocument {

    /**
     * 查询内容
     */
    private String con;

    /**
     * 字段
     */
    private String col;

    /**
     * 表名
     */
    private String tab;

    /**
     * 描述
     */
    private String des;

    /**
     * 主键内容拼接字符串
     */
    private String pri;

    /**
     * 字段别名
     */
    private String ali;

    /**
     * 访问路径
     */
    private String url;

    /**
     * 获取内容
     * @return 内容
     * */
    @Override
    public String getCon() {
        return con;
    }

    /**
     * 赋值内容
     * @param  con 内容
     * */
    @Override
    public void setCon(String con) {
        this.con = con;
    }

    /**
     * 获取字段
     * @return 字段
     * */
    @Override
    public String getCol() {
        return col;
    }

    /**
     * 赋值字段
     * @param  col 字段
     * */
    @Override
    public void setCol(String col) {
        this.col = col;
    }

    /**
     * 获取表名
     * @return 表名
     * */
    @Override
    public String getTab() {
        return tab;
    }

    /**
     * 赋值表名
     * @param  tab 表名
     * */
    @Override
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     * 获取描述
     * @return 描述
     * */
    @Override
    public String getDes() {
        return des;
    }

    /**
     * 赋值描述
     * @param  des 描述
     * */
    @Override
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * 获取主键内容拼接字符串
     * @return 主键内容拼接字符串
     * */
    @Override
    public String getPri() {
        return pri;
    }

    /**
     * 赋值主键内容拼接字符串
     * @param  pri 主键内容拼接字符串
     * */
    @Override
    public void setPri(String pri) {
        this.pri = pri;
    }

    /**
     * 获取字段别名
     * @return 字段别名
     * */
    @Override
    public String getAli() {
        return ali;
    }

    /**
     * 赋值字段别名
     * @param  ali 字段别名
     * */
    @Override
    public void setAli(String ali) {
        this.ali = ali;
    }

    /**
     * 获取访问路径
     * @return 访问路径
     * */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * 赋值访问路径
     * @param  url 访问路径
     * */
    @Override
    public void setUrl(String url) {
        this.url = url;
    }
}
