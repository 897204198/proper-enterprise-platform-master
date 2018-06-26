package com.proper.enterprise.platform.oopsearch.api.model;

import com.proper.enterprise.platform.oopsearch.api.enums.DataBaseType;
import com.proper.enterprise.platform.oopsearch.api.enums.SyncMethod;

/**
 * 同步文档模型
 * 用于从数据库，向mongodb中同步数据时，存储数据对象
 */
public class SyncDocumentModel {

    /**
     * 更新前内容
     */
    private String conb;

    /**
     * 更新后内容
     */
    private String cona;

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
     * 是否已处理 false:未处理 true:已处理
     */
    private boolean process;

    /**
     * 字段别名
     */
    private String alias;

    /**
     * 调用url
     */
    private String url;

    /**
     * 数据类型
     */
    private DataBaseType dataBaseType;

    /**
     * 同步方法
     */
    private SyncMethod method;

    /**
     * 获取更新前内容
     *
     * @return 更新前内容
     */
    public String getConb() {
        return conb;
    }

    /**
     * 赋值更新前内容
     *
     * @param conb 更新前内容
     */
    public void setConb(String conb) {
        this.conb = conb;
    }

    /**
     * 获取更新后内容
     *
     * @return 更新后内容
     */
    public String getCona() {
        return cona;
    }

    /**
     * 赋值更新后内容
     *
     * @param cona 更新后内容
     */
    public void setCona(String cona) {
        this.cona = cona;
    }

    /**
     * 获取字段
     *
     * @return 字段
     */
    public String getCol() {
        return col;
    }

    /**
     * 赋值字段
     *
     * @param col 字段
     */
    public void setCol(String col) {
        this.col = col;
    }

    /**
     * 获取表名
     *
     * @return 表名
     */
    public String getTab() {
        return tab;
    }

    /**
     * 赋值表名
     *
     * @param tab 表名
     */
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    public String getDes() {
        return des;
    }

    /**
     * 赋值描述
     *
     * @param des 描述
     */
    public void setDes(String des) {
        this.des = des;
    }

    /**
     * 获取主键内容拼接字符串
     *
     * @return 主键内容拼接字符串
     */
    public String getPri() {
        return pri;
    }

    /**
     * 赋值主键内容拼接字符串
     *
     * @param pri 主键内容拼接字符串
     */
    public void setPri(String pri) {
        this.pri = pri;
    }

    /**
     * 获取是否已处理
     *
     * @return 是否已处理
     */
    public boolean isProcess() {
        return process;
    }

    /**
     * 赋值是否已处理
     *
     * @param process 是否已处理
     */
    public void setProcess(boolean process) {
        this.process = process;
    }

    /**
     * 获取别名
     *
     * @return 别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 赋值别名
     *
     * @param  alias 别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取访问路径
     *
     * @return 访问路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 赋值访问路径
     *
     * @param  url 访问路径
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取数据类型
     *
     * @return 数据类型
     */
    public DataBaseType getDataBaseType() {
        return dataBaseType;
    }

    /**
     * 赋值数据类型
     *
     * @param dataBaseType 数据类型
     */
    public void setDataBaseType(DataBaseType dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    /**
     * 获取同步方法
     *
     * @return 同步方法
     */
    public SyncMethod getMethod() {
        return method;
    }

    /**
     * 赋值同步方法
     *
     * @param method 同步方法
     */
    public void setMethod(SyncMethod method) {
        this.method = method;
    }
}
