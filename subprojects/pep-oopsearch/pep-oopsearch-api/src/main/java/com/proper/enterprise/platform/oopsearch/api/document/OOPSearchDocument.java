package com.proper.enterprise.platform.oopsearch.api.document;

/**
 * 查询文档接口，mongodb使用jpa时，定义的document需实现该接口，方便其他模块调用
 * */
public interface OOPSearchDocument {

    /**
     * 获取内容
     * @return 内容
     * */
    String getCon();

    /**
     * 赋值内容
     * @param  content 内容
     * */
    void setCon(String content);

    /**
     * 获取字段
     * @return 字段
     * */
    String getCol();

    /**
     * 赋值字段
     * @param  column 字段
     * */
    void setCol(String column);

    /**
     * 获取表名
     * @return 表名
     * */
    String getTab();

    /**
     * 赋值表名
     * @param  table 表名
     * */
    void setTab(String table);

    /**
     * 获取描述
     * @return 描述
     * */
    String getDes();

    /**
     * 赋值描述
     * @param  desc 描述
     * */
    void setDes(String desc);

    /**
     * 获取主键内容拼接字符串
     * @return 主键内容拼接字符串
     * */
    String getPri();

    /**
     * 赋值主键内容拼接字符串
     * @param  pri 主键内容拼接字符串
     * */
    void setPri(String pri);
}
