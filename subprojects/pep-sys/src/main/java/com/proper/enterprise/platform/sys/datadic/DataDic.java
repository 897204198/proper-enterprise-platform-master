package com.proper.enterprise.platform.sys.datadic;

import java.io.Serializable;

/**
 * 数据字典
 * 表示系统内编码和名称的对应关系
 * 数据字典按照类别进行归类，通常是 key-value 的扁平结构
 */
public interface DataDic extends Serializable {

    /**
     * 获得数据字典类别
     * @return 数据字典类别
     */
    String getCatalog();

    /**
     * 设置数据字典类别
     * @param catalog
     */
    void setCatalog(String catalog);

    /**
     * 获得编码
     * @return 编码
     */
    String getCode();

    /**
     * 设置编码
     * @param code 编码
     */
    void setCode(String code);

    /**
     * 获得名称
     * @return 名称
     */
    String getName();

    /**
     * 设置名称
     * @param name 名称
     */
    void setName(String name);

    /**
     * 获得顺序
     * @return
     */
    int getOrder();

    /**
     * 设置顺序
     * @param order 顺序
     */
    void setOrder(int order);

}
